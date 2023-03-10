function getData() {
    var resL = document.getElementById('login').value;
    var spanAlertL = document.getElementById('inLogin');

    var resP = document.getElementById('password').value;
    var spanAlertP = document.getElementById('inPassword');

    if (resL.length < 3) {
        spanAlertL.textContent = 'Логин должжен быть длинне 3 символов';
        //alert('Login must contain at least 3 characters');
    } else {
        spanAlertL.textContent = 'Ваш логин: ' + resL;
        //alert(resL);
    }

    if (resP.length < 3) {
        spanAlertP.textContent = 'Пароль должжен быть длинне 3 символов';
        //alert('Password must contain at least 3 characters');
    } else {
        spanAlertP.textContent = 'Ваш пароль: ' + resP;
        //alert(resP);
    }
}


var subButton = document.getElementById('saveData');
subButton.addEventListener('click', getData, false); 