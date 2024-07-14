# EasyCook (Tso-Hue 後端)
![image](https://github.com/user-attachments/assets/4da0e816-976f-437d-b72a-7a7bb5413560)

Tsohue 是一個食譜分享與食材包販售平台，讓客戶可以根據食譜訂購食材包。我們解決了日常中採購會產生的許多問題，包括在採購餐點的不便和減少剩餘食材的浪費。本repo僅描述關於後端服務的啟動流程，完整專案運行請參考以下相關連結:

E-commerce platform | Website | Front-end: https://github.com/cce932/tsohue

E-commerce platform | App: https://github.com/amy5563891/EasyCookMobileApp

Backstage Content Management | Website | Front-end: https://github.com/cce932/tsohue-manager

Back-end: https://github.com/ShannonHung/EasyCook

# 基本設定
在下載該檔案的時候建議先下載maven並且設定環境變數
參考連結: [「入門篇」「java神器」使用maven搭建spring boot](https://kknews.cc/code/emazpyq.html)

# Postman連結
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/c0d616b0c3ca05d9ffec#?env%5BEasyCook-RealAddr%5D=W3sia2V5IjoiYWRkciIsInZhbHVlIjoiaHR0cDovLzE0MC4xMTguOS4xNDU6ODA4MiIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoidG9rZW4iLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWV9XQ==)
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
2. 去`/login`取得token
3. 去`/auth/parse` Post json 去解析 token 樣式如下，查看目前登入的這個帳號的資料以及權限
```json
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




# `/login` api 取得Token流程
## 若成功會回傳200 OK 並且回傳token(如下)
```json
{
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJVc2VySW5mbyI6Im1lbWJlcjAwMiIsImV4cCI6MTYxMTQ2MjMxMiwiaXNzIjoiU2hhbm5vbkh1bmcgRnJvbSBFYXN5Q29vayJ9.BmG28RK9-2_dJd3LJlhswjNiykGM3wyFICIosVkB9ik"
}
```
## 登入失敗的狀況有以下幾種
- 格式錯誤,必須符合以下格式
- 帳號或密碼錯誤

> 回應格式
```json
{
  "account": "username",
  "password": "password"
}
```
> Unauthorized 401錯誤回應
```json
{
    "status": "UNAUTHORIZED",
    "timestamp": {
        "nano": 132550600,
        "year": 2021,
        "monthValue": 1,
        "dayOfMonth": 24,
        "hour": 12,
        "minute": 27,
        "second": 0,
        "dayOfWeek": "SUNDAY",
        "dayOfYear": 24,
        "month": "JANUARY",
        "chronology": {
            "id": "ISO",
            "calendarType": "iso8601"
        }
    },
    "message": "LOGIN FAILURE", //可能帳號或密碼錯或request格式不正確
    "debugMessage": "Full authentication is required to access this resource"
}
```

# http status 錯誤
## 401 登入失敗
1. `/login` 失敗 可能因為帳號密碼有誤或是格式不正確
> 401 Unauthorized
```json
{
    "status": "UNAUTHORIZED",
    "timestamp": {
        "nano": 132550600,
        "year": 2021,
        "monthValue": 1,
        "dayOfMonth": 24,
        "hour": 12,
        "minute": 27,
        "second": 0,
        "dayOfWeek": "SUNDAY",
        "dayOfYear": 24,
        "month": "JANUARY",
        "chronology": {
            "id": "ISO",
            "calendarType": "iso8601"
        }
    },
    "message": "LOGIN FAILURE", //可能帳號或密碼錯或request格式不正確
    "debugMessage": "Full authentication is required to access this resource"
}
```



## 403 權限問題
1. `/api/member/allMembers` 只能employee才可以存取，但若權限不足的使用者進行REQUEST會回傳403錯誤如下
```JSON
{
    "status": "FORBIDDEN",
    "timestamp": "24-01-2021 12:34:24",
    "message": "NEED AUTHORIZATION",
    "debugMessage": "Access is denied"
}
```

## 400 Bad Request
### JWT Token Expired
如果TOKEN超過期限則會拒絕REQUEST並且回應DEBUG MESSAGE `JWT  expired...`內容
```json
{
    "status": "BAD_REQUEST",
    "timestamp": {
        "nano": 237947000,
        "year": 2021,
        "monthValue": 1,
        "dayOfMonth": 24,
        "hour": 12,
        "minute": 31,
        "second": 12,
        "dayOfWeek": "SUNDAY",
        "dayOfYear": 24,
        "month": "JANUARY",
        "chronology": {
            "id": "ISO",
            "calendarType": "iso8601"
        }
    },
    "message": "Unexpected error",
    "debugMessage": "JWT expired at 2021-01-24T04:13:27Z. Current time: 2021-01-24T04:31:12Z, a difference of 1065236 milliseconds.  Allowed clock skew: 0 milliseconds."
}
```
### Header的Authorization存在，並為空值: Bearer Token
會回傳以下錯誤
```json
{
    "status": "BAD_REQUEST",
    "timestamp": {
        "nano": 357116800,
        "year": 2021,
        "monthValue": 1,
        "dayOfMonth": 24,
        "hour": 12,
        "minute": 38,
        "second": 4,
        "dayOfWeek": "SUNDAY",
        "dayOfYear": 24,
        "month": "JANUARY",
        "chronology": {
            "id": "ISO",
            "calendarType": "iso8601"
        }
    },
    "message": "Unexpected error",
    "debugMessage": "JWT String argument cannot be null or empty."
}
```
### JSON 格式有誤
如果有以下問題就會回傳`"message": "Malformed JSON request."`
- 沒有json內容就進行post
- json的格式有問題像是少一個冒號
> 沒有傳json就進行post傳json就進行post
```json
{
    "status": "BAD_REQUEST",
    "timestamp": "24-01-2021 12:52:46",
    "message": "Malformed JSON request.",
    "debugMessage": "Required request body is missing: public org.springframework.http.ResponseEntity<com.seminar.easyCookWeb.model.user.MemberResponse> com.seminar.easyCookWeb.controller.user.MemberController.createMember(com.seminar.easyCookWeb.model.user.MemberRequest)"
}
```

> request json裡面的key: account, password格式有問題
```json
//account不小心打成account1
{
  "account1": 123,
  "password1": 123,
  "phone1": 123,
  "usernam1e": 123
}
//錯誤回應
{
    "status": "BAD_REQUEST",
    "timestamp": "24-01-2021 01:01:36",
    "message": "Malformed JSON request.",
    "debugMessage": "Account or Password is Empty"
}
```


## 409 Confict
- 如果資料庫已經有該使用者會回傳以下錯誤
```json
{
    "status": "CONFLICT",
    "timestamp": "24-01-2021 12:51:23",
    "message": "ACCOUNT DUPLICATED",
    "debugMessage": null
}
```

## 404 NotFound
- 沒有此api路徑
```json
{
    "timestamp": "2021-01-24T05:37:48.191+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "",
    "path": "/api/auth/login"
}
```

# 建立cicd
.drone.yml
Dockerfile
