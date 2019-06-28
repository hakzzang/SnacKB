package hbs.com.snackb.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*

class JJwtHelper {
    val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)!!

    fun convertJWS(accessKey: String, secretKey: String): String =
        Jwts.builder()
            .claim("access_key",accessKey) //UPBIT API의 access key
            .claim("nonce", Date(System.currentTimeMillis())) //nonce를 만들기 위해서 현재시간을 뽑음.
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray(Charsets.UTF_8))//HS256알고리즘으로 UTF8형태로 뽑아냅니다.
            .compact()//토큰을 만듦
}