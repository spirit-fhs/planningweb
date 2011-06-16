// This function changes the color of a table-cell in wishtimes-table

function changeColor(){
  for(i = 0; i<document.getElementsByTagName("input").length;i++){
    if(document.getElementsByTagName("input")[i].checked == true) {
      if(document.getElementsByTagName("input")[i].value == "W") document.getElementsByTagName("input")[i].parentNode.parentNode.style.backgroundColor = "#90EE90";
      if(document.getElementsByTagName("input")[i].value == "K") document.getElementsByTagName("input")[i].parentNode.parentNode.style.backgroundColor = "#FFEC8B";
      if(document.getElementsByTagName("input")[i].value == "N") document.getElementsByTagName("input")[i].parentNode.parentNode.style.backgroundColor = "#F8F8FF";
    }
  }
}