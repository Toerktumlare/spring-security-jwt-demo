# Spring Security + JWT tutorial project
This repository contains a Spring boot application using Spring Security configured as a resource server which 
demonstrates the handling of JWTs with different type of claims.

### General info

- Java 17
- Maven 3

# Test JWTs
All JWTs below can be used against the service with different results. All tokens are generated from 
(jwt.io) [http://jwt.io] using the private key `jwtRS256.rsa.key` and `jwtRS256.rsa.pem`. All JWTs have the same 
header, what differs is in the body. Below is an example of the decoded JWT. All encoded JWTs in this file is based of 
the de-encoded one below with small differences. We use the `jwtRS256.pkcs8.pem` public key to verify the signature in
our spring application.

You can check these differences if you do a get request with one of the tokens against the [http://localhost:8080/token]
(http://localhost:8080/token) endpoint, and it will return the token that the service has parsed in a slightly more 
readable format (principal).

> If you want to use the Role based JWTs you need to start the service with the profile "roles" enabled. See the
> `application.yml` in the `src/main/resources/` folder

## Example decoded jwt

JOSE Header:
```json
{
  "alg": "RS256",
  "typ": "JWT"
}
```
JWT body:
```json
{
  "iss": "http://foobar.com",
  "sub": "foo",
  "aud": "foobar",
  "name": "Mr Foo Bar",
  "scope": "read",
  "iat": 1516239022,
  "exp": 2500000000
}
```

## Scope based JWTs
This token contains a `scope` claim with the authority `read`, can be used on the `/read` endpoint.
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vZm9vYmFyLmNvbSIsInN1YiI6ImZvbyIsImF1ZCI6ImZvb2JhciIsIm5hbWUiOiJNciBGb28gQmFyIiwic2NvcGUiOiJyZWFkIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjI1MDAwMDAwMDB9.Oz2NDpN5-Ypq-a-cbjE-JH9E6S1Sya2ZsnuW9qRbFwNcCrqln3uGGyly6rWc6JKMD0ZHbf-kshuI1-ka4b2TPdFcYNCtOIgA4m_EiR3EecJMmjiINNGmRP8J7Mh83PRoKKVQOOKMhbAvB2J6jexB-kEGKlea92Ls6BgY0fuTgszkji8N12dZ4DsWNWBN2-0AFJAK7w9yogQOTFieWmP6NMmB8PPbR0WJqLFvkSjhaq2RR9-vxaSd2YyMAyGMDFrOV72-XbEenaKdzNY-kkknZvADRilta5T4EaaRnI2oI5GPX-UvIXiO_bqhgvXdy_2JRE9Zo3s55SVpE2JKWGHkZxDoKegdpSyaBaTvdvh_31kuQ6MbqD_XCemaUfXRCQlerNqk6OekG_aZWptd--jCUYNKhDCBiHuzifRE7TFECciJFlV7QyRPFcfV_an-UB0Cfm5CUfS1eXkLAFgvWNkOfIKt4kWHU0CsDnXLAPvnhqoqy4dbp4pBQuy5rnyktcE0dEGagwzGxccY1tAbuvvmikQU6axo7oid9Emu7kwll30yhE-C01gvAsbmFeaYKtgk3SgdtYztJY9dRKMrVeEE-Lb9I34XjnoBOutKgKHtSE5vPq99zDPHm7NkQuD6zibzKtCDlnQpx18SPKid-3lvOr9Im0zj04jusGmC8_kiTcI
```

While this token has both `read` and `write` scope, so it can be used on both the `/read` and `/write` endpoint.
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vZm9vYmFyLmNvbSIsInN1YiI6ImZvbyIsImF1ZCI6ImZvb2JhciIsIm5hbWUiOiJNciBGb28gQmFyIiwic2NvcGUiOiJyZWFkIHdyaXRlIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjI1MDAwMDAwMDB9.QOBDxzjN16cgKBhf7nWk4DRs1vDnajYnVMijlwfJ_b8f7_gRJa5z3N-hNyK_S0vFR12SWrDRtaeuDqTCGjteoYO6xBwVq0hfyowieb9vK5ekTKfkIhxGIAosHfzNyXBguaYLCPo-ihh-vgmUaRtZXCBVjHWtcMEeWfoiTm8D0LhJJPj4s0TfC3DVJj9s1gr8W7EFXvR4Or6M-yOGOr39QZV8S-rMuVECs26AHWmkDJ1C1aF5_02mHY33T-csR7t7t9PQfZ35VNCin7yr35buDKCFbcVFt5OE_if5LqJouT3qCLBXkSIGqQy1u601QwDKbQS9dXfnvAjp7VvHibe9n89QJtJ526OY5EpnaW4QrfXfKFzF1oxfTtCNxn2qbi2bHB2YBT4AUkxVD3B5tjMjhkpwwweETa_VjrQvM7BJlz3VEl1d2V99C50p1s_HfTCxkwLaDK6JFRgaHSpIY63bb41p8_VgQlDO-maHjjN5_YoEUSV3dVWizvrn2RsmrM5FLszKCGGdksHSlGvGocsrUgZ27q0d1quKL0K1jrUSrUFV89QnHkspifqAUJqRjO4UO1qJ-1gcaKX7gMS7O1g7nAnMDlMLAwuNQ4vcEJanG2Bw7Lc-NTyp1UIsn56Rj22YjbwUOZx5NNnPHjVvxRMBLxujtTZl5oHA6FQP9NsSqD4
```

## Role based JWTs
To use the role based JWT you need to run the application with the profile `roles` activated. You can set this in
the `application.yml`.

### JWT token with no scope, but authorities
This token has a custom claim called `authorities` which contains the authority `user`. It can be used on the
`/user` endpoint.
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vZm9vYmFyLmNvbSIsInN1YiI6ImZvbyIsImF1ZCI6ImZvb2JhciIsIm5hbWUiOiJNciBGb28gQmFyIiwiYXV0aG9yaXRpZXMiOiJ1c2VyIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjI1MDAwMDAwMDB9.B_jALTJmN3_dDMvYzoFsvFfgm-cnXxG5f1UfH5-d7BoO6OaoRY1jOGRFLBzdMO35Us4AY7-ppp3q0xCPFsFs8gI91iLB4jgxFwN2dNYchx7yu7cu5A8BA3u0Rx5bCg2F1MJIgMxR3p02yQy3x20F_VR_s5EdBQLvGGxzfxsxjBEmX2c_m-L7s4DM50T7HxfVsig1I9MhgOQoxvx-ovLSVSVSp256vub5pPnBZ5_E-V1kbAQ3BJBaFkICDFSKawVXVFdOTAYmlC9Ap5TrXNevt8gHgEh9ZWIWNB16w6OMmS7uMkfJOb0a-P0-0jSgfoS-_Lkh4R8ZlV4V6vEdnco14FarWOAYq3EmqYYJ1qDrZU26WRqPuy8Rzmytn7chZ7EZ389fbGZBIVknv2QSBXHSItyujZfw90FXY9gfyg464T8MZc57NkqdQycdh2nJHBlSdNoDz5MTzUkV0oBPeMAlKD07HvzmFafEW8h4uo7nmNH3EkvHIHZZACl0VNhAS9olv4bAsSpIClx9p65UVbKnyqfdOOLNbgjT0Q9ELLNqD29vxrebUSAmfd3ecSSMUdnn2clFlXPisRA13-PomQeMwEt05PLI3q0dzu-KfPxZsiDwwd59wZI9SINnvuSuUg2ujXksUB_JKb1h9g0DoedEVnB9U9ENuCU6QBdfgZ-Qyrk
```

### JWT token with no scope but multiple values in authorities
This token contains a custom claim called `authorities` which contains 2 authorities, space separated, `user admin`
which means it can be used on both the `/user` endpoint but also the `/admin` endpoint.
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vZm9vYmFyLmNvbSIsInN1YiI6ImZvbyIsImF1ZCI6ImZvb2JhciIsIm5hbWUiOiJNciBGb28gQmFyIiwiYXV0aG9yaXRpZXMiOiJ1c2VyIGFkbWluIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjI1MDAwMDAwMDB9.QgefB__AkWGuMFnvbLZJitjNGnY43vi7matAR3HEZcWU3ENLzj6P2FQcxsYQ_s6CCSZLgE_nBiAD3xbeSDYOqzPoLtHvOORsXSEjUCP8EcmBs62-jAVUI0nWGDywoBhT48mqltQbqfapjEumORMTX7MWdiN9KJDqXxhWckj_Q-rQzJkSIDNM9fBvUGkNRF1nH4xIDqJ2qbcJGK129LlfvXHui87xBmzqqpvXdhcxuelV0n6WjZO1GowKRFlod3FF5RwRY3sggO00BCCOi9nrzDqBgP-WgcKcTXl-NpFHZ6yyODdcJkp21_1c7rzwxj23xm2zB2S9OvUU-Y8g1UzTBtuo8fs4CxpPyuOYg1kNaKwv9A1oHbxF8s-NnYE1eq1VFjVLgiIfhJKk4QySPPtXs8roP3Yu_gWYWHHRjDkCBVimCvnjS4KgFctEzxR57d7uBwyONz4Yq7vp_8CkPYWeP-cSupa35GzPz4eI5wumENun0OrkpLqkdO7J2n7gKHHOJsil0ozeQBMQ_CFhvWXnXT9pAm5V6Jp0gH4d6SJwV0iyYWkUoylbeXHM-imXvI63mfPZ8oqRhv6zj3DqXtKntZWSvzZ73bbmvpo1QPfXGO_bc9jq0YM_5C8wutEDX3H6pJBREKwGTBk9Wujzs8jxsuNzwSPagLRXCl3McvH7Mo4
```

## No authority JWT
This token contains no `scope` or `authority` claim. So it can only be used on the `/token` endpoint.
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vZm9vYmFyLmNvbSIsInN1YiI6ImZvbyIsImF1ZCI6ImZvb2JhciIsIm5hbWUiOiJNciBGb28gQmFyIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjI1MDAwMDAwMDB9.Wajf18Oq4OtJ8q4ynfZJ3IrZt91cRk53Z7RM3o7P0RIoa3NXOil7Hrc648pEf1L8GJKaugx2jJRDqGG6FaQnhJ8ycT3FDP9C2kahTg1qWbW5TINrK-8kPXZ4YZgE7ad4eKBKzWLTjhTNWc5rCAmFgNW8CYWE8mIG9kt5KLN-UB6lC0cyRfrGuKIF5OQ7YN1_yVRMEa0gGz_3TdlhFhmi-oGuu-8dCPOBaEtmUO-Q_BSeKxI1Hdgg48sJwJ0iiZMFHfthPWcf5IKB0kiRca2Mm2tTYCPrdkLjs33H9RLbIqe5LkBudCu7TEki1pXhbyg-7SkzYTcMDt24wmwMtdwI42wtmPJmPdA9A4HGnK0r-o2VCYcAueHRDETtMvE-fyjCHOd3EZLfIKNeVodemjNxaJ-FHf1cfHJYMqy7Ij9h561tffbUNHIfXYU9AEBHE4NMO8PTGpIDCUo1w-X3BDi6p6Sb4cmKab5ntc9ZMRBbsaubK5YY-u3Mc9-uNyy-u9JHmOaqnBDhK5kTNl9hFS9eAN8fEfOQWsaN7Iq27x1LIT-G5aladW-VHr9U4PysjsKXU9ThfOR9tWekX5-VgXG26osD5M6VS404OBdc1tqz34JVuApmxXnG10J1YKRn5q7alP9OyMn2z_RnHX9d9ZJUzihME8qFFS-Os1A0vDhGvf8
```
