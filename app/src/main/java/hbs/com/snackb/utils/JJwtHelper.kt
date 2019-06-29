package hbs.com.snackb.utils

import android.os.Build
import androidx.annotation.RequiresApi
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*
import java.util.Base64.getEncoder



class JJwtHelper {
    val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)!!

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertJWS(accessKey: String, secretKey: String): String {
        val dd = Jwts.builder()
            .claim("apikey",accessKey) //UPBIT API의 access key
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray(Charsets.UTF_8))//HS256알고리즘으로 UTF8형태로 뽑아냅니다.
            .compact()//토큰을 만듦

        return Base64.getEncoder().encodeToString(dd.toByteArray())
    }

}