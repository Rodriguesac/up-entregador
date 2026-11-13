const CACHE='up-gestor-v1';
const CORE=['/','/index.html','/manifest.webmanifest'];
self.addEventListener('install',e=>e.waitUntil(caches.open(CACHE).then(c=>c.addAll(CORE)).then(()=>self.skipWaiting())));
self.addEventListener('activate',e=>e.waitUntil(caches.keys().then(keys=>Promise.all(keys.filter(k=>k!==CACHE).map(k=>caches.delete(k)))).then(()=>self.clients.claim())));
self.addEventListener('fetch',e=>{
 const req=e.request;
 if(req.method!=='GET'||new URL(req.url).origin!==self.location.origin)return;
 e.respondWith(fetch(req).then(res=>{
   if(res.ok&&['document','manifest'].includes(req.destination)){
     const copy=res.clone();
     caches.open(CACHE).then(c=>c.put(req,copy)).catch(()=>{});
   }
   return res;
 }).catch(()=>caches.match(req).then(found=>found||Response.error())));
});
