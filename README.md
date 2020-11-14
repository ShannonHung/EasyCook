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


### Swagger操作
`http://localhost:8082/swagger-ui.html`
操作說明
1. 啟動localhost server之後, 去瀏覽器採訪上面的uri
2. 可以看到所有api接口的說明

[詳細圖片Trello: 網頁基礎設定/ 建立API文件檔案Swagger附件](https://trello.com/c/zVwpKjZp)

### MapStruct
如果使用Intellij，請先Plugin MapStruct的功能

### 目前權限說明
前端在進行request的時候不需要放role因為後端已經預設好`employee/register`給予的權限就是`ROLE_EMPLOYEE`，目前`ROLE_EMPLOYEE`的權限可以訪問所有的API, 如果前端透過`member/register`來註冊員工帳號，後端預設給他的權限是`ROLE_MEMBER`，目前甚麼API都不能查看除了自己的個人資料

### 操作流程說明
1. 先去`employee/register`註冊員工帳號 (詳細post規則可以查看swagger), 也可以去`member/register`註冊會員帳號
2. 去`/auth/login`取得token
3. 去`/auth/parse` Post json 去解析 token 樣式如下，查看目前登入的這個帳號的資料以及權限
```
//request
{
    "accessToken": "Bearer 請在這裡放Token"
}
//the example of response for employee
{
    "iss": "ShannonHung From EasyCook", //發token的單位
    "UserInfo": { //使用者資訊
        "account": "employee001", //使用者帳號
        "username": "shannonhung", // 使用者名稱
        "phone": "0978232062", //使用者電話
        "email": "micky@gmail.com", //使用者信箱
        "role": "EMPLOYEE", //使用者權限
        "id": 2, //使用者id
        "department": "Sales", //使用者部門
        "title": "經理" //使用者職稱
    },
    "exp": 1604207180 //過期的期限為五分鐘，如果時間過了就要重新LOGIN一次
}
```
4. 確認response 中 UserInfo的Role為EMPLOYEE表示這個TOKEN權限合法
3. 當要透過api的獲取資料的時候，拿Bearer Token放到header屬性key為`Authentication`的value裡面 
請參考 [Header Authentication - Key Value Edit](https://trello-attachments.s3.amazonaws.com/5eecde56c5e18058021a384f/5f9b00bea46dd915a390227d/fac572a50466ff0d48da71b735b2f7f4/image.png) 就可以進行成功的Request囉~~



