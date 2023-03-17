var params = window
    .location
    .search
    .replace('?','')
    .split('&')
    .reduce(
        function(p,e){
            var a = e.split('=');
            p[ decodeURIComponent(a[0])] = decodeURIComponent(a[1]);
            return p;
        },
        {}
    );
//console.log(params['tgid']);

// POST запрос на подтверждение аутентификации
const postData = async (url = '', data = {}) => {
    // Формируем запрос
    const response = await fetch(url, {
      method: 'POST', // Метод, если не указывать, будет использоваться GET
      headers: {  // Заголовок запроса
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)  // Данные
    });
    return response.json(); 
  }


function getData() {
  var resL = document.getElementById('login').value;
  var resP = document.getElementById('password').value;
  
  var pAlert = document.getElementById('forErrors');
  pAlert.textContent = "";

  if (resL.length < 3) {
    pAlert.textContent += 'Логин должен быть длинне 3 символов\n';
    //alert('Login must contain at least 3 characters');
  } 
  else if (resP.length < 3) {
    pAlert.textContent += 'Пароль должен быть длинне 3 символов\n';
    //alert('Password must contain at least 3 characters');
  } 
  else {
    var resp = postData('', { tgid: params['tgid'] });
    //  .then((data) => { 
    //    console.log(data); 
    //    pAlert.textContent = data;
    //  });
    console.log(resp); 
    pAlert.textContent = resp;
  }
}


var subButton = document.getElementById('saveData');
subButton.addEventListener('click', getData, false); 