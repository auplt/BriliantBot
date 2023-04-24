function toggleLoader() {
  const loader = document.getElementById('loader')
  loader.classList.toggle('hidden')
}

function onSuccess(formNode) {
  alert('Авторизация выполнена!')
}

function onUnnecessary(formNode) {
  alert('Вы уже авторизованы!')
}

function onMistake(formNode) {
  alert('Не верный логин или пароль!')
}

function onError(error) {
  alert(error.message)
}

function serializeForm(formNode) {
  const data = new FormData(formNode)
  return data
}

function checkValidity(event) {
  const formNode = event.target.form
  const isValid = formNode.checkValidity()
  formNode.querySelector('button').disabled = !isValid
}

async function sendData(data) {
  return await fetch('http://127.0.0.1:5001/brilliantbot/api/auth', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: new URLSearchParams(data),
  })
}

async function sendSession(data) {
  return await fetch('http://127.0.0.1:8002/telegrambot/api/session', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: new URLSearchParams(data),
  })
}

async function handleFormSubmit(event) {
  toggleLoader()
  event.preventDefault()
  const data = serializeForm(event.target)

  let response = await sendData(data);

  if (response.status === 200) {
    let res = await response.json();
    if (JSON.parse(JSON.stringify(res)).success === true) {

      var session = {
        'id': urlParams.get('id'),
        'login': JSON.parse(JSON.stringify(res)).login,
        'token': JSON.parse(JSON.stringify(res)).token,
        'end_date': JSON.parse(JSON.stringify(res)).end_date
      }

      let response = await sendSession(session);

      if (response.status === 200) {
        let res = await response.json()
        if (JSON.parse(JSON.stringify(res)).success === true) {
          toggleLoader();
          onSuccess(event.target);
          toggleLoader();

        }
        else {
          toggleLoader();
          onMistake(event.target);
          toggleLoader();
        }
      }
      else {
        toggleLoader();
        onError(res.error);
        toggleLoader();
      }
    }
    else if ('status' in res) {
      toggleLoader();
      onUnnecessary(event.target);
      toggleLoader();
    }
    else {
      toggleLoader();
      onMistake(event.target);
      toggleLoader();
    }
  }
  else {
    toggleLoader();
    onError(response.error);
    toggleLoader();
  }
  document.getElementById("authorize").reset();
  toggleLoader();
}

const QueryString = window.location.search;
const urlParams = new URLSearchParams(QueryString);

const applicantForm = document.getElementById('authorize')
applicantForm.addEventListener('submit', handleFormSubmit)
applicantForm.addEventListener('input', checkValidity)

applicantForm.querySelector('button').disabled = true