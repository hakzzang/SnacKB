package hbs.com.snackb.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.NoSuchAlgorithmException
import java.security.InvalidKeyException


class HMACUtils {
    // hash 알고리즘 선택
    private val ALGOLISM = "HmacSHA256"
    // hash 암호화 key
    private val key = "nsHc6458"


    fun hget(message: String): String {
        try {
            // hash 알고리즘과 암호화 key 적용
            val hasher = Mac.getInstance(ALGOLISM)
            hasher.init(SecretKeySpec(key.toByteArray(), ALGOLISM))

            // messages를 암호화 적용 후 byte 배열 형태의 결과 리턴
            val hash = hasher.doFinal(message.toByteArray())
            return byteToString(hash)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }

        return ""
    }

    // byte[]의 값을 16진수 형태의 문자로 변환하는 함수
    private fun byteToString(hash: ByteArray): String {
        val buffer = StringBuffer()

        for (i in hash.indices) {
            var d = hash[i].toInt()
            d += if (d < 0) 256 else 0
            if (d < 16) {
                buffer.append("0")
            }
            buffer.append(Integer.toString(d, 16))
        }
        return buffer.toString()
    }
}
