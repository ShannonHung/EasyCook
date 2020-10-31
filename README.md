# EasyCook

# 基本設定
在下載該檔案的時候建議先下載maven並且設定環境變數
參考連結: [「入門篇」「java神器」使用maven搭建spring boot](https://kknews.cc/code/emazpyq.html)


- 參考連結: [「入門篇」「java神器」使用maven搭建spring boot](https://kknews.cc/code/emazpyq.html)
- localhost:8082/h2 的資料庫進入方式
    1. url: jdbc:h2:mem:easyCook
    2. username: sa
    3. password: (不用打直接進入)
- Servert Port: localhost:8082


### Getting start with VScode

1. Get an extension, 'Lombok'

2. If you do not have Maven, go https://maven.apache.org/download.cgi# and install it

3. Install dependences
```
mvn install
```
4. Press F5

5. Do post in postman `http://localhost:8082/member` to get a set of account.

6. Go `localhost:{server.port}`
   `server.port` is in `EasyCook/src/main/resources/application.properties`

7. Login in with the account that you got it in step 3.

8. You can see data with h2 in `http://localhost:{server.port}/h2`

