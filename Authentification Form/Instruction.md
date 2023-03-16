# Просто http сервер (и никаких проблем)

## документация
https://www.npmjs.com/package/http-server


## установить необходимую библиотеку
npm install http-server -g


## запуск http сервера 
http-server


## запуск https сервера
http-server -S -C cert.pem
--port 8081                     // будет 8081 порт


## адрес сервера
выводится в консоли

стандартные:
https://192.168.56.1:8080
https://192.168.1.105:8080
https://127.0.0.1:8080



# Включить https запросы (протокол SSL)

## официальный репозиторий OpenSSL
https://github.com/openssl/openssl


## инструкция по установке OpenSSL
https://itsecforu.ru/2020/03/11/kak-ustanovit-ssl-na-windows-10/


## инструкция по генерации SSL ключа
https://www.npmjs.com/package/http-server


