/**
 * 
 */
localStorage.openpages = Date.now();
var onLocalStorageEvent = function(e){
  if(e.key == "openpages"){
    // Emit that you're already available.
    localStorage.page_available = Date.now();
  }
  if(e.key == "page_available"){
    alert("Ya existe una pagina abierta en este browser!");
  var win = window.open("about:blank", "_self");

  }
};
window.addEventListener('storage', onLocalStorageEvent, false);