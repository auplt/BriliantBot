function getData() {
    var resL = document.getElementById('login').value;
    var resP = document.getElementById('password').value;
    
  var pAlert = document.getElementById('forErrors');
  pAlert.textContent = "";

    if (resL.length < 3) {
        pAlert.textContent += 'Логин должен быть длинне 3 символов';
        //alert('Login must contain at least 3 characters');
    } 

    if (resP.length < 3) {
        pAlert.textContent += '\nПароль должен быть длинне 3 символов';
        //alert('Password must contain at least 3 characters');
    } 
  
}


var subButton = document.getElementById('saveData');
subButton.addEventListener('click', getData, false); 