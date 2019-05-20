# ont-sourcing-2c

## 接口

### 存证接口

* [存证](#存证)
* [二维码](#二维码)

* [根据hash取证](#根据hash取证)
* [根据hash删除存证](#根据hash删除存证)

* [获取存证总条数](#获取存证总条数)
* [获取存证历史记录](#获取存证历史记录)

* [浏览器存证历史记录](#浏览器存证历史记录)
* [浏览器根据hash取证](#浏览器根据hash取证)


### 附
* [错误码](#错误码)

## 接口规范


### 存证

```text
url：/api/v1/c/attestation/put
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
	"metadata": {
            "name":"",
            "Title": "",
            "Tags": "",
            "Description":"",
            "Timestamp": "",
            "Location":""
	},
	"context": {
	    "image": ["",""],
	    "text": ["",""]
	},
	"signature":"",
	"type": "TEXT"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| filehash   | String | 文件hash    |
| filehash   | String | 文件hash    |
| metadata   | JSON |     |
| context   | JSON |     |
| signature   | String |     |
| type   | String | PDF/TEXT/IMAGE/VIDEO   |

- 响应：

```json
{
    "desc": "SUCCESS",
    "error": 0,
    "version": "1.0.0",
    "result": "/api/v1/c/attestation/cyano/ca4e952e3f96fb4c1b800153bf6d2c8c648b88371f613d13ea8ac75f29048f29",
    "action": "putAttestation"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true，失败返回""     |
| version    | String | 版本号                        |

### 二维码

```text
url：/attestation/cyano/{hash}
method：GET
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| hash   | String |     |

- 响应：

```json
{
    "desc": "SUCCESS",
    "action": "getAttestationCyanoInfo",
    "result": {
        "action": "invoke",
        "version": "v1.0.0",
        "id": "294683bc-6af7-42b3-ab54-0a20d719961a",
        "params": {
            "invokeConfig": {
                "contractHash": "8fe3eabb062637986be6e3f6fa034504d10e51e2",
                "functions": [
                    {
                        "operation": "putRecord",
                        "args": [
                            {
                                "name": "key",
                                "value": "7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375"
                            },
                            {
                                "name": "value",
                                "value": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
                            }
                        ]
                    }
                ],
                "payer": null,
                "gasLimit": 20000,
                "gasPrice": 500
            }
        }
    },
    "error": 0,
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true，失败返回""     |
| version    | String | 版本号                        |



### 根据hash取证

```text
url：/api/v1/c/attestation/hash
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"hash":"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:57.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "ee973d13c6ed2d8c7391223b4fb6f5c785f402d81d41b02ab7590113cbb00752",
            "createTime": "2019-04-22T07:32:57.000+0000",
            "updateTime": null,
            "height": 1621684
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY1234",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:24.000+0000",
            "timestampSign": "960ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "1ab4b5b2c6c89b4f1a553b7aef30c3f3ef203a323d23cd383261cc6d0df73870",
            "createTime": "2019-04-22T07:32:25.000+0000",
            "updateTime": null,
            "height": 1621682
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T04:22:55.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "0437084a4f6204aad88fa1507fc13a44f83cecf44fc925692f9bc43f23e52fc3",
            "createTime": "2019-04-22T04:22:57.000+0000",
            "updateTime": null,
            "height": 1621275
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "selectByOntidAndHash",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号                        |


### 根据hash删除存证

```text
url：/api/v1/c/attestation/hash
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"hash":"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "version": "1.0.0",
    "error": 0,
    "action": "deleteByOntidAndHash",
    "result": true,
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true     |
| version    | String | 版本号                        |



### 获取存证总条数

```text
url：/api/v1/c/attestation/count
method：POST
```

- 请求：

```json
{
		"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| ontid   | String | 需要查询的ontid    |
| access_token   | String | access_token    |



- 响应：

```json
{
    "result": 6,
    "error": 0,
    "desc": "SUCCESS",
    "action": "count",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回总条数，失败返回""
| version    | String | 版本号                     

### 获取存证历史记录

```text
url：/api/v1/c/attestation/history
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTU5OTMzNjAsImlhdCI6MTU1NTkwNjk2MCwianRpIjoiMTYwY2FkNjNmZTdkNGY5MTk3NGFjZjQzYWNlMzkzNmYiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE5MzE3ODk4ODU2MGQ5NGQ3MTBmZTc2Mzg1ZWE0OWRiMmRjZjczZmU2NjAyYjU0NjI2YWE0MmJmZWYwYTFkYTE0ODI5YWVmYTJjNjNlMTA5N2Y2ZjM0YTJlMTJmOGYwNWNmYzRhZWI3NzlkOWEwMWY2NDY1Y2VjYWM1MzNjYjk5Ng",
	"pageNum": 1,
	"pageSize": 3,
	"type":"INDEX"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |
| type   | String | INDEX/TEXT/IMAGE/VIDEO，空表示所有类型   |


- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"name\":\"img1\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888\",\"message\":\"\"},{\"name\":\"img2\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999\",\"message\":\"\"},{\"name\":\"img3\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000\",\"message\":\"\"}]",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:50:45.000+0000",
            "timestampSign": "950ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "261944dfe5f5e83cac5d9b4c1065f508d7750e66adec85d12cb7415ef5cc1d3a",
            "createTime": "2019-04-22T07:50:46.000+0000",
            "updateTime": null,
            "height": 1621724
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "null",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:47:22.000+0000",
            "timestampSign": "950ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "99c240e7860a6016dd38de4d5fc73ea6ed533c76007a4b314741259b54f0937a",
            "createTime": "2019-04-22T07:47:23.000+0000",
            "updateTime": null,
            "height": 1621714
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "null",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:42:54.000+0000",
            "timestampSign": "960ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "4a70100fa6c5ff6b2b5484493c2c147861772821c50df27db28b01b13bc3a593",
            "createTime": "2019-04-22T07:42:55.000+0000",
            "updateTime": null,
            "height": 1621704
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "getExplorerHistory",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号                     


### 浏览器存证历史记录

```text
url：/api/v1/c/attestation/explorer
method：POST
```

- 请求：

```json
{
    "pageNum": 1,
    "pageSize": 3
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-24T09:22:35.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "7116936d9992ce4cbb7f3531d4e3e1807201cc6163d9d5958e375f27d604889a",
            "createTime": "2019-04-24T09:22:35.000+0000",
            "updateTime": null,
            "height": 1628962
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"url\":\"http://....\"}]",
            "type": "IMAGE",
            "timestamp": "2019-04-23T03:49:20.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000",
            "txhash": "ef12912c68d20812acb08a0ab6a0809c85f95ebef1bd8315a3e5f891e3d12688",
            "createTime": "2019-04-23T03:49:22.000+0000",
            "updateTime": null,
            "height": 1624385
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"name\":\"img1\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888\",\"message\":\"\"},{\"name\":\"img2\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999\",\"message\":\"\"},{\"name\":\"img3\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000\",\"message\":\"\"}]",
            "type": "INDEX",
            "timestamp": "2019-04-23T03:49:20.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "89e9ba952dc618229f7ab575c5c85863783fad26a1e65790be843481436cfe07",
            "createTime": "2019-04-23T03:49:21.000+0000",
            "updateTime": null,
            "height": 1624385
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "version": "1.0.0",
    "action": "getExplorer"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号              


### 浏览器根据hash取证

```text
url：/api/v1/c/attestation/explorer/hash
method：POST
```

- 请求：

```json
{
	"hash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "result": [
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:57.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "ee973d13c6ed2d8c7391223b4fb6f5c785f402d81d41b02ab7590113cbb00752",
            "createTime": "2019-04-22T07:32:57.000+0000",
            "updateTime": null,
            "height": 1621684
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY1234",
            "companyOntid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:24.000+0000",
            "timestampSign": "960ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "1ab4b5b2c6c89b4f1a553b7aef30c3f3ef203a323d23cd383261cc6d0df73870",
            "createTime": "2019-04-22T07:32:25.000+0000",
            "updateTime": null,
            "height": 1621682
        },
        {
            "ontid": "did:ont:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyOntid": "",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T04:22:55.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "0437084a4f6204aad88fa1507fc13a44f83cecf44fc925692f9bc43f23e52fc3",
            "createTime": "2019-04-22T04:22:57.000+0000",
            "updateTime": null,
            "height": 1621275
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "selectByOntidAndHash",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号                        |


## 错误码

| 返回代码  | 描述信息   | 备注                   |
|:-----------|:-------|:------------------------------|
| 0      | SUCCESS | 成功 |
| 61001      | INVALID_PARAMS | 参数错误 |
| 71001      | ONTID_EXIST | ontid错误 |
| 71002      | ONTID_NOT_EXIST | ontid错误 |
| 80001      | BLOCKCHAIN_ERROR | 本体链错误 |
| 90001      | SFL_ERROR | 司法链错误 |
| 100000      | INTERNAL_SERVER_ERROR | 服务器内部错误 |

