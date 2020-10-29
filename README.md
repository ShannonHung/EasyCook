# EasyCook

# 環境建置
在下載該檔案的時候建議先下載maven並且設定環境變數
參考連結: [「入門篇」「java神器」使用maven搭建spring boot](https://kknews.cc/code/emazpyq.html)

### Getting start with VScode

1. Get an extension, 'Lombok'

2. If you do not have Maven, go https://maven.apache.org/download.cgi# and install it

3. install dependence
```
maven install
```

4. Do post in postman `http://localhost:8082/member` to get a set of account.

5. Go `localhost:{server.port}`
   `server.port` is in `EasyCook/src/main/resources/application.properties`

6. Login in with the account that you got it in step 3.

7. You can see data with h2 in `http://localhost:{server.port}/h2`
