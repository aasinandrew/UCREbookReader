function changefont() {

   var newStyle=document.createElement('style'); 
   newStyle.appendChild(document.createTextNode("@font-face{font-family:'Amerika'; src:url('file:///android_asset/fonts/Amerika.ttf')}"));
   document.head.appendChild(newStyle); 
   document.body.style.fontFamily = "Amerika";
}
