import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import {createClient} from "npm:@supabase/supabase-js@2.56.1";

// STAGING ONLY — deploy to Rodrigues Açaí e Cia store Supabase only after
// independent UP is fully connected. The receipt token never reaches UP.
const enc=new TextEncoder();
const txt=(x:unknown)=>String(x??"").trim();
const UUID=/^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
function b64url(data:Uint8Array){
 let binary="";for(const b of data)binary+=String.fromCharCode(b);
 return btoa(binary).replace(/\+/g,"-").replace(/\//g,"_").replace(/=/g,"");
}
function send(status:number,obj:Record<string,unknown>,origin:string){
 const headers:Record<string,string>={
 "content-type":"application/json; charset=utf-8","cache-control":"no-store",
 "vary":"Origin"
 };
 if(origin)headers["access-control-allow-origin"]=origin;
 return new Response(JSON.stringify(obj),{status,headers});
}
async function sha256(value:string){
 const hash=await crypto.subtle.digest("SHA-256",enc.encode(value));
 return Array.from(new Uint8Array(hash),b=>b.toString(16).padStart(2,"0")).join("");
}
function tokenMatch(raw:any,access:string,hash:string){
 const plain=txt(raw?._guestAccessToken),stored=txt(raw?._guestAccessTokenHash);
 return (stored.length===64&&stored===hash) || (plain.length>=30&&plain===access);
}
const publicWebsite=new Set([
 "https://rodriguesacaiecia.web.app",
 "https://rodriguesacaiecia.firebaseapp.com"
]);
Deno.serve(async(req:Request)=>{
 const origin=txt(req.headers.get("origin"));
 if(!origin||!publicWebsite.has(origin))return send(403,{ok:false,error:"origin_not_allowed"},"");
 if(req.method==="OPTIONS")return new Response(null,{status:204,headers:{
  "access-control-allow-origin":origin,"access-control-allow-headers":"apikey,authorization,content-type",
  "access-control-allow-methods":"POST,OPTIONS","vary":"Origin"
 }});
 if(req.method!=="POST")return send(405,{ok:false,error:"method_not_allowed"},origin);
 if(Deno.env.get("UP_CUSTOMER_TRACKING_ENABLED")!=="true")
  return send(503,{ok:false,error:"independent_tracking_disabled"},origin);
 const url=Deno.env.get("SUPABASE_URL")||"",
   service=Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")||"",
   secret=Deno.env.get("UP_TRACKING_SECRET")||"";
 if(!url||!service||secret.length<32)
  return send(503,{ok:false,error:"tracking_service_not_configured"},origin);
 let raw:string;
 try{raw=await req.text()}catch{return send(400,{ok:false,error:"invalid_request"},origin)}
 if(enc.encode(raw).length>3000)return send(413,{ok:false,error:"payload_too_large"},origin);
 let input:any;try{input=JSON.parse(raw)}catch{return send(400,{ok:false,error:"invalid_json"},origin)}
 const receipt=txt(input?.receipt_token),
  access=txt(input?.access_token),
  lookup=txt(input?.lookup_access_token),
  orderRef=txt(input?.order_id),
  bearer=txt(req.headers.get("authorization")).replace(/^Bearer\s+/i,"");
 const validReceipt=receipt.length>=20&&receipt.length<=300;
 const validLookup=lookup.length>=30&&lookup.length<=300;
 const validAccess=access.length>=30&&access.length<=300;
 if(!validReceipt&&!validLookup&&!validAccess&&!bearer)
  return send(401,{ok:false,error:"order_credential_required"},origin);
 const db=createClient(url,service,{auth:{persistSession:false,autoRefreshToken:false}});
 const fields="id,firebase_id,customer_id,status,receipt_token,fulfillment_type,tracking_enabled,raw_payload";
 try{
  let order:any=null;
  if(validReceipt){
   const result=await db.from("orders").select(fields)
    .eq("receipt_token",receipt).maybeSingle();
   if(result.error)throw result.error;
   order=result.data;
  }else if(validLookup){
   // Token temporario da recuperacao de pedido por numero e telefone.
   const id=await sha256(lookup);
   const found=await db.from("app_documents").select("data")
    .eq("collection_name","public_order_lookup_tokens")
    .eq("document_id",id).maybeSingle();
   if(found.error)throw found.error;
   const info=found.data?.data||{};
   if(UUID.test(txt(info.order_id))&&Date.parse(txt(info.expiresAt))>Date.now()){
    const result=await db.from("orders").select(fields)
     .eq("id",info.order_id).maybeSingle();
    if(result.error)throw result.error;
    order=result.data;
   }
  }else if(validAccess){
   // Token secreto de um pedido guest-order, verificado no banco da loja.
   if(orderRef.length>=5&&orderRef.length<=180){
    const refColumn=UUID.test(orderRef)?"id":"firebase_id";
    const found=await db.from("orders").select(fields)
     .eq(refColumn,orderRef).maybeSingle();
    if(found.error)throw found.error;
    const hash=await sha256(access);
    if(found.data&&tokenMatch(found.data.raw_payload,access,hash))
     order=found.data;
   }
  }else if(bearer&&orderRef.length>=5&&orderRef.length<=180){
   // Usuario autenticado so visualiza pedidos do proprio customer_id.
   const auth=await db.auth.getUser(bearer);
   const uid=txt(auth.data?.user?.id);
   if(uid){
    const customer=await db.from("customers").select("id")
     .eq("auth_user_id",uid).maybeSingle();
    if(customer.error)throw customer.error;
    if(customer.data?.id){
     const refColumn=UUID.test(orderRef)?"id":"firebase_id";
     const found=await db.from("orders").select(fields)
      .eq(refColumn,orderRef)
      .eq("customer_id",customer.data.id).maybeSingle();
     if(found.error)throw found.error;
     order=found.data;
    }
   }
  }
  if(!order)return send(404,{ok:false,error:"order_not_found_or_access_denied"},origin);
  const bridge=order.raw_payload?.up_bridge;
  const jobId=txt(bridge?.jobId);
  if(bridge?.mode!=="isolated"||!UUID.test(jobId)||
     !["ENTREGA","DELIVERY"].includes(txt(order.fulfillment_type).toUpperCase())||
     order.tracking_enabled!==true||
     ["ENTREGUE","CANCELADO","CANCELADA","CANCELLED","FINALIZADO"].includes(txt(order.status).toUpperCase()))
    return send(409,{ok:false,error:"live_tracking_not_available"},origin);
  // UP will itself check that its job is in the customer-facing route phase.
  // No personally identifying information is embedded in this short ticket.
  const now=Math.floor(Date.now()/1000);
  const payload={version:1,aud:"up-customer-tracking",
   order_id:order.id,job_id:jobId,iat:now,exp:now+75,jti:crypto.randomUUID()};
  const data=b64url(enc.encode(JSON.stringify(payload)));
  const key=await crypto.subtle.importKey("raw",enc.encode(secret),
   {name:"HMAC",hash:"SHA-256"},false,["sign"]);
  const signature=new Uint8Array(await crypto.subtle.sign("HMAC",key,enc.encode(data)));
  return send(200,{ok:true,ticket:data+"."+b64url(signature),expires_in:75},origin);
 }catch(e){console.error("[UP customer ticket]",(e as Error)?.message?.slice(0,120)||"internal");
  return send(503,{ok:false,error:"tracking_temporarily_unavailable"},origin)}
});
