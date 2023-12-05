# 获取 AccessToken

## authorization_code 模式

1. 发起 /oauth/authorize 请求获取 code

![微信截图_20190806143303.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073195511-b3655988-37d0-4026-b64b-dc7d14f4ba77.png#align=left&display=inline&height=456&name=%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20190806143303.png&originHeight=456&originWidth=1602&size=31432&status=done&width=1602)

> 如果客户端开启了自动认证，会自动跳转到 redirect_uri 地址。如果没有会跳转到用户授权界面，用户同意授权后跳转到 redirect_uri 地址。


2. redirect_uri 获取到 code，发起 /oauth/token 请求，获取 accessToken

![1.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073269555-cedff6fa-90f2-4f06-af66-fc9e8203d824.png#align=left&display=inline&height=500&name=1.png&originHeight=500&originWidth=1597&size=38711&status=done&width=1597)

3. 拿到 accessToken

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.ffMbB2IQWCKcQycuEtHxQpjjFtBc6RSfgfOkrVdzQZ6Pfptwji8dP_-nwQcKIAF-rrJeyQzpTCii45iQYj2ElmYALKqAzKxRTv4x8c6wvA0ejoaCa2oadmbEdDNy4EdDrqhNugAh4LPWdzF1V3g20nZexoPRMNjGlk9kJjAXMQoQSTYZom4AB8wWMGz7ZTP2_V3GBx5XNiKU-zXgQ33eEfxUgbg7Is65_PgIv18JTBGl-GLZ86GtvNCMre4U7xtyeNeZ3hCxRBq6lvvhri-DYXePKM6GiJCR7Y4P9216ohr4MV4f05XIy0kkOPLL_UlZar4KjP9PhIBmOEM4Yg6tmQ",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJhdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJkNWYyMWQ2Ny02YmJkLTRkMjUtODA4NS1mYjVjNjI3MGZjOTIiLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.Ts0_kRXhppkNn0yLvw9sAQkcRCVDZAQgW7uxN0yRzf5f3644fXgxEujA6iSWD9hc_HXxNty2hbLQfXXktyUpKbpeQsWmLXdVx6eTTQiUY2ktXeCXh0MazQB0pci2RvYJCLCaDEaRaXRqab1LpLW_UsRZ8WNHf-IjcdBo5t-oaSuGeMY_X1wwqugYIkyENQane7VPVY71l6EvxpGgBxaxuhyHN1uvJ9Q1kvGuOsgdqgItIIylOFu9t-VZcuhoRTs4lq2ywZIMwj3Tsp7LcwUe5NuArXK22yokAzgDlnUCB3WFCy76hKkVZX6K6sD6K5yKlPfkQ4Ghkg3LkEh-7p9o_g",
    "expires_in": 86399,
    "scope": "All",
    "jti": "e2783163-72fd-4dd8-94cf-4d147cb8c747",
    "license": "made by taroco",
    "x-user-name": "admin",
    "x-user-id": 909620761
}
```

## password 模式

1. 发起 /oauth/token 请求，获取 accessToken

![2.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073309005-40f00b98-a0e5-44a6-91a4-7d5aa54c7e63.png#align=left&display=inline&height=498&name=2.png&originHeight=498&originWidth=1589&size=36427&status=done&width=1589)

> scope 为可选参数，不传的话会返回所有客户端 scope


2. 拿到 accessToken

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.ffMbB2IQWCKcQycuEtHxQpjjFtBc6RSfgfOkrVdzQZ6Pfptwji8dP_-nwQcKIAF-rrJeyQzpTCii45iQYj2ElmYALKqAzKxRTv4x8c6wvA0ejoaCa2oadmbEdDNy4EdDrqhNugAh4LPWdzF1V3g20nZexoPRMNjGlk9kJjAXMQoQSTYZom4AB8wWMGz7ZTP2_V3GBx5XNiKU-zXgQ33eEfxUgbg7Is65_PgIv18JTBGl-GLZ86GtvNCMre4U7xtyeNeZ3hCxRBq6lvvhri-DYXePKM6GiJCR7Y4P9216ohr4MV4f05XIy0kkOPLL_UlZar4KjP9PhIBmOEM4Yg6tmQ",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJhdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJkNWYyMWQ2Ny02YmJkLTRkMjUtODA4NS1mYjVjNjI3MGZjOTIiLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.Ts0_kRXhppkNn0yLvw9sAQkcRCVDZAQgW7uxN0yRzf5f3644fXgxEujA6iSWD9hc_HXxNty2hbLQfXXktyUpKbpeQsWmLXdVx6eTTQiUY2ktXeCXh0MazQB0pci2RvYJCLCaDEaRaXRqab1LpLW_UsRZ8WNHf-IjcdBo5t-oaSuGeMY_X1wwqugYIkyENQane7VPVY71l6EvxpGgBxaxuhyHN1uvJ9Q1kvGuOsgdqgItIIylOFu9t-VZcuhoRTs4lq2ywZIMwj3Tsp7LcwUe5NuArXK22yokAzgDlnUCB3WFCy76hKkVZX6K6sD6K5yKlPfkQ4Ghkg3LkEh-7p9o_g",
    "expires_in": 86399,
    "scope": "All",
    "jti": "e2783163-72fd-4dd8-94cf-4d147cb8c747",
    "license": "made by taroco",
    "x-user-name": "admin",
    "x-user-id": 909620761
}
```

## client_credentials模式

1. 发起 /oauth/token 请求，获取 accessToken

![3.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073337950-ab1dae00-7e56-464e-8091-71463171333d.png#align=left&display=inline&height=380&name=3.png&originHeight=380&originWidth=1593&size=27271&status=done&width=1593)

> scope 为可选参数，不传的话会返回所有客户端 scope
> client_credentials 模式会跳过用户授权步骤，直接返回 accessToken


2. 拿到 accessToken

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.ffMbB2IQWCKcQycuEtHxQpjjFtBc6RSfgfOkrVdzQZ6Pfptwji8dP_-nwQcKIAF-rrJeyQzpTCii45iQYj2ElmYALKqAzKxRTv4x8c6wvA0ejoaCa2oadmbEdDNy4EdDrqhNugAh4LPWdzF1V3g20nZexoPRMNjGlk9kJjAXMQoQSTYZom4AB8wWMGz7ZTP2_V3GBx5XNiKU-zXgQ33eEfxUgbg7Is65_PgIv18JTBGl-GLZ86GtvNCMre4U7xtyeNeZ3hCxRBq6lvvhri-DYXePKM6GiJCR7Y4P9216ohr4MV4f05XIy0kkOPLL_UlZar4KjP9PhIBmOEM4Yg6tmQ",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJhdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJkNWYyMWQ2Ny02YmJkLTRkMjUtODA4NS1mYjVjNjI3MGZjOTIiLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.Ts0_kRXhppkNn0yLvw9sAQkcRCVDZAQgW7uxN0yRzf5f3644fXgxEujA6iSWD9hc_HXxNty2hbLQfXXktyUpKbpeQsWmLXdVx6eTTQiUY2ktXeCXh0MazQB0pci2RvYJCLCaDEaRaXRqab1LpLW_UsRZ8WNHf-IjcdBo5t-oaSuGeMY_X1wwqugYIkyENQane7VPVY71l6EvxpGgBxaxuhyHN1uvJ9Q1kvGuOsgdqgItIIylOFu9t-VZcuhoRTs4lq2ywZIMwj3Tsp7LcwUe5NuArXK22yokAzgDlnUCB3WFCy76hKkVZX6K6sD6K5yKlPfkQ4Ghkg3LkEh-7p9o_g",
    "expires_in": 86399,
    "scope": "All",
    "jti": "e2783163-72fd-4dd8-94cf-4d147cb8c747",
    "license": "made by taroco",
    "x-user-name": "admin",
    "x-user-id": 909620761
}
```

## implicit 模式

1. 发起 /oauth/authorize 请求，获取 accessToken

![4.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073377460-1e5307b4-f8b6-47d7-ac15-eb2382edeefa.png#align=left&display=inline&height=481&name=4.png&originHeight=481&originWidth=1589&size=34248&status=done&width=1589)

> implicit 模式为 get 请求，需要用户登录
> accessToken 直接返回给 redirect_uri，implicit 模式不安全，一般不建议采用此种模式


## mobile 模式

1. 发起 /oauth/mobile 请求，获取 accessToken

![5.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073440113-8bf06427-b9f3-4585-ad46-50b6ad6dab58.png#align=left&display=inline&height=343&name=5.png&originHeight=343&originWidth=1588&size=21871&status=done&width=1588)

![6.png](https://cdn.nlark.com/yuque/0/2019/png/193443/1565073446442-2c696cb4-0712-4aea-8e61-22ac06e11bdd.png#align=left&display=inline&height=417&name=6.png&originHeight=417&originWidth=1586&size=24425&status=done&width=1586)

> header Authorization 的值是 "clientId:client_secret"，取 Base64 编码的值。
> 通过 mobile 获取 accessToken，需要先获取手机号发送的验证码 code。


2. 拿到 accessToken

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.ffMbB2IQWCKcQycuEtHxQpjjFtBc6RSfgfOkrVdzQZ6Pfptwji8dP_-nwQcKIAF-rrJeyQzpTCii45iQYj2ElmYALKqAzKxRTv4x8c6wvA0ejoaCa2oadmbEdDNy4EdDrqhNugAh4LPWdzF1V3g20nZexoPRMNjGlk9kJjAXMQoQSTYZom4AB8wWMGz7ZTP2_V3GBx5XNiKU-zXgQ33eEfxUgbg7Is65_PgIv18JTBGl-GLZ86GtvNCMre4U7xtyeNeZ3hCxRBq6lvvhri-DYXePKM6GiJCR7Y4P9216ohr4MV4f05XIy0kkOPLL_UlZar4KjP9PhIBmOEM4Yg6tmQ",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsaWNlbnNlIjoibWFkZSBieSB0YXJvY28iLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIkFsbCJdLCJhdGkiOiJlMjc4MzE2My03MmZkLTRkZDgtOTRjZi00ZDE0N2NiOGM3NDciLCJ4LXVzZXItaWQiOjkwOTYyMDc2MSwiZXhwIjoxNTY0NjQ2NzM4LCJ4LXVzZXItbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiT1BfVVNFUiJdLCJqdGkiOiJkNWYyMWQ2Ny02YmJkLTRkMjUtODA4NS1mYjVjNjI3MGZjOTIiLCJjbGllbnRfaWQiOiI1ZDIyZWI2ZThiMGM3YmEwNjYwMTQzOTgifQ.Ts0_kRXhppkNn0yLvw9sAQkcRCVDZAQgW7uxN0yRzf5f3644fXgxEujA6iSWD9hc_HXxNty2hbLQfXXktyUpKbpeQsWmLXdVx6eTTQiUY2ktXeCXh0MazQB0pci2RvYJCLCaDEaRaXRqab1LpLW_UsRZ8WNHf-IjcdBo5t-oaSuGeMY_X1wwqugYIkyENQane7VPVY71l6EvxpGgBxaxuhyHN1uvJ9Q1kvGuOsgdqgItIIylOFu9t-VZcuhoRTs4lq2ywZIMwj3Tsp7LcwUe5NuArXK22yokAzgDlnUCB3WFCy76hKkVZX6K6sD6K5yKlPfkQ4Ghkg3LkEh-7p9o_g",
    "expires_in": 86399,
    "scope": "All",
    "jti": "e2783163-72fd-4dd8-94cf-4d147cb8c747",
    "license": "made by taroco",
    "x-user-name": "admin",
    "x-user-id": 909620761
}
```

