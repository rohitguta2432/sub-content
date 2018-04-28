function FindProxyForURL(url, host)
{
// variable strings to return
var proxy_yes = "PROXY 10.248.241.20:8080";
var proxy_no = "DIRECT";
if (shExpMatch(host, "*.scb.co.th*")) { return proxy_no; }
if (shExpMatch(host, "*localhost*")) { return proxy_no; }
// Proxy anything else
return proxy_yes;
}
