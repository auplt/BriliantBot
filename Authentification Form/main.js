function getData() {
const form = document.querySelector('.form');
        var resL = document.getElementById('login').value;
        var spanAlertL = document.getElementById('inLogin');

        var resP = document.getElementById('password').value;
        var spanAlertP = document.getElementById('inPassword');
        const saveButton = document.getElementById('saveData');

    if (resL.length < 3) {
        spanAlertL.textContent = 'Логин должен быть длиннеe 3 символов';
        //alert('Login must contain at least 3 characters');
    } else {
        spanAlertL.textContent = 'Ваш логин: ' + resL;
        //alert(resL);
    }

    if (resP.length < 3) {
        spanAlertP.textContent = 'Пароль должен быть длиннеe 3 символов';
        //alert('Password must contain at least 3 characters');
    } else {
        spanAlertP.textContent = 'Ваш пароль: ' + resP;
        //alert(resP);
    }
    saveButton.addEventListener('click', (event) => {
            event.preventDefault();

            const formData = new FormData(form);

            fetch('/Authentification%20Form/', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
        });
}
