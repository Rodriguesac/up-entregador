/**
 * Rodrigues Açaí e Cia — mapa do UP Entregas para o dono do pedido.
 * Instalar no próprio domínio rodriguesacaiecia.web.app.
 *
 * O recibo fica SOMENTE no browser e vai apenas ao Supabase da loja.
 * A API UP recebe um ticket HMAC curto sem dados do comprador.
 * A posição aparece apenas com corrida atribuída e GPS recente.
 */
const STORE_TICKET =
  "https://fdqqwdplprzpqufpgdrm.supabase.co/functions/v1/up-customer-ticket";
const UP_TRACKING =
  "https://gkwmqwtktedarodvqqtn.supabase.co/functions/v1/up-customer-tracking";
const ALLOWED = new Set([
  "https://rodriguesacaiecia.web.app",
  "https://rodriguesacaiecia.firebaseapp.com"
]);
const TERMINAL = new Set(["DELIVERED", "CANCELLED", "FAILED"]);
const delay = 12_000;

let leafletReady = null;
function loadLeaflet() {
  if (window.L) return Promise.resolve(window.L);
  if (leafletReady) return leafletReady;
  leafletReady = new Promise((resolve, reject) => {
    const css = document.createElement("link");
    css.rel = "stylesheet";
    css.href = "https://unpkg.com/leaflet@1.9.4/dist/leaflet.css";
    css.integrity = "sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoqREi7Y7xJdM4=";
    css.crossOrigin = "";
    const script = document.createElement("script");
    script.src = "https://unpkg.com/leaflet@1.9.4/dist/leaflet.js";
    script.integrity = "sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo=";
    script.crossOrigin = "";
    script.onload = () => window.L ? resolve(window.L) : reject(Error("leaflet_unavailable"));
    script.onerror = () => reject(Error("leaflet_unavailable"));
    document.head.append(css, script);
  }).catch(error => { leafletReady = null; throw error; });
  return leafletReady;
}

function validPoint(value) {
  const lat = Number(value?.lat), lng = Number(value?.lng);
  return Number.isFinite(lat) && Number.isFinite(lng) &&
    lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180 &&
    !(lat === 0 && lng === 0) ? {lat, lng} : null;
}

async function post(url, data, signal, authorization = "") {
  const response = await fetch(url, {
    method: "POST",
    credentials: "omit",
    mode: "cors",
    cache: "no-store",
    headers: {
      "Content-Type": "application/json",
      ...(authorization ? {"Authorization": "Bearer " + authorization} : {})
    },
    body: JSON.stringify(data),
    signal
  });
  const body = await response.json().catch(() => ({}));
  if (!response.ok || body.ok !== true) {
    const error = new Error(String(body.error || "tracking_unavailable"));
    error.code = response.status;
    throw error;
  }
  return body;
}

/**
 * mountUpCustomerTracking({container, onStatus, ...credencial})
 * Credenciais aceitas (uma por vez):
 * - receiptToken: recibo secreto do pedido;
 * - lookupAccessToken: token recebido em public-order-lookup;
 * - accessToken + orderId: chave do pedido convidado (guest-order);
 * - authToken + orderId: sessão Supabase do proprietário do pedido.
 * Retorna função de limpeza, chamar ao sair do histórico.
 */
export function mountUpCustomerTracking({
  container, receiptToken = "", lookupAccessToken = "",
  accessToken = "", orderId = "", authToken = "", onStatus = () => {}
}) {
  if (!(container instanceof HTMLElement)) throw Error("container_required");
  if (!ALLOWED.has(location.origin)) {
    container.textContent = "Rastreamento disponível apenas no site da loja.";
    return () => {};
  }
  const validReceipt = typeof receiptToken === "string" && receiptToken.length >= 20;
  const validLookup = typeof lookupAccessToken === "string" && lookupAccessToken.length >= 30;
  const validGuest = typeof accessToken === "string" && accessToken.length >= 30 && orderId;
  const validAuth = typeof authToken === "string" && authToken.length >= 30 && orderId;
  if (!validReceipt && !validLookup && !validGuest && !validAuth) {
    container.textContent = "Este pedido não possui acesso autorizado ao rastreamento.";
    return () => {};
  }
  const ticketBody = validReceipt ? {receipt_token: receiptToken} :
    validLookup ? {lookup_access_token: lookupAccessToken} :
    validGuest ? {access_token: accessToken, order_id: orderId} : {order_id: orderId};
  const ticketAuth = validReceipt || validLookup || validGuest ? "" : authToken;

  const root = document.createElement("section");
  root.setAttribute("aria-label", "Rastreamento do pedido");
  root.style.cssText = "border-radius:14px;overflow:hidden;background:#f5f5f7;color:#222;padding:12px;font:14px system-ui";
  const status = document.createElement("p");
  status.setAttribute("role", "status");
  status.style.cssText = "margin:4px 0 10px;font-weight:600";
  const mapElement = document.createElement("div");
  mapElement.style.cssText = "height:280px;border-radius:10px;overflow:hidden;display:none";
  const note = document.createElement("small");
  note.textContent = "Posição disponível somente durante a entrega; atualização aproximada a cada 12 segundos.";
  note.style.cssText = "display:block;margin-top:9px;color:#575757";
  const fallback = document.createElement("a");
  fallback.target = "_blank";
  fallback.rel = "noopener noreferrer";
  fallback.textContent = "Abrir posição no mapa";
  fallback.style.cssText = "display:none;margin-top:8px;color:#4f2995";
  root.append(status, mapElement, fallback, note);
  container.replaceChildren(root);

  let stopped = false, timer = null, map = null, marker = null;
  let aborter = null, ticket = "", expiresAt = 0;
  let loadingMap = false;
  const message = (text, code) => {
    if (stopped) return;
    status.textContent = text;
    onStatus(code);
  };
  message("Buscando a localização do entregador…", "loading");

  async function show(point) {
    if (stopped) return;
    const gps = validPoint(point);
    if (!gps) return;
    const url = new URL("https://www.openstreetmap.org/");
    url.searchParams.set("mlat", String(gps.lat));
    url.searchParams.set("mlon", String(gps.lng));
    url.searchParams.set("zoom", "16");
    fallback.href = url.toString();
    fallback.style.display = "inline-block";
    message("Motoboy a caminho • localização recente", "live");
    if (loadingMap) return;
    loadingMap = true;
    try {
      const L = await loadLeaflet();
      if (stopped) return;
      if (!map) {
        map = L.map(mapElement, {zoomControl: true}).setView([gps.lat, gps.lng], 16);
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          maxZoom: 19, attribution: "© OpenStreetMap contributors"
        }).addTo(map);
        marker = L.marker([gps.lat, gps.lng]).addTo(map).bindPopup("Motoboy UP Entregas");
        mapElement.style.display = "block";
        map.invalidateSize();
      } else {
        mapElement.style.display = "block";
        map.invalidateSize();
        marker.setLatLng([gps.lat, gps.lng]);
        map.panTo([gps.lat, gps.lng], {animate: true, duration: .5});
      }
    } catch {
      // Link externo permanece disponível mesmo se Leaflet/CDN falhar.
      message("Localização recebida. Abra no mapa.", "link_available");
    } finally {
      loadingMap = false;
    }
  }

  async function poll() {
    if (stopped) return;
    if (document.visibilityState !== "visible") {
      timer = setTimeout(poll, delay);
      return;
    }
    aborter = new AbortController();
    try {
      if (!ticket || Date.now() > expiresAt - 15_000) {
        const response = await post(STORE_TICKET,
          ticketBody, aborter.signal, ticketAuth);
        ticket = response.ticket;
        expiresAt = Date.now() + Math.min(75, Number(response.expires_in) || 60) * 1000;
      }
      const response = await post(UP_TRACKING, {ticket}, aborter.signal);
      if (stopped) return;
      if (TERMINAL.has(response.status)) {
        message("Esta entrega foi encerrada.", "finished");
        return; // Finaliza o acompanhamento, sem posicionamento falso.
      }
      if (validPoint(response.tracking)) {
        await show(response.tracking);
      } else {
        mapElement.style.display = "none";
        fallback.style.display = "none";
        message("Aguardando posição recente do motoboy.", "no_recent_gps");
      }
    } catch (error) {
      if (stopped) return;
      if (error.code === 401) ticket = "";
      if (error.code === 409) {
        message("Rastreamento disponível após a saída do motoboy.", "not_available");
      } else {
        message("Rastreamento temporariamente indisponível.", "offline");
      }
      mapElement.style.display = "none";
      fallback.style.display = "none";
    } finally {
      aborter = null;
      if (!stopped) timer = setTimeout(poll, delay);
    }
  }
  poll();
  return () => {
    stopped = true;
    clearTimeout(timer);
    aborter?.abort();
    map?.remove();
    root.remove();
  };
}
