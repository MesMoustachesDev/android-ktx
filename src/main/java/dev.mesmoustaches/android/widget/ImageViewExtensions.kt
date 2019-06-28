package android.widget

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import timber.log.Timber
import java.lang.Exception
import java.util.*

//fun ImageView.setBarCodeImageBitmap(code: String) {
//    post {
//        val formatter = MultiFormatWriter()
//        val matrix = formatter.encode(code, BarcodeFormat.CODE_128, measuredWidth, measuredHeight)
//        setImageBitmap(BarcodeEncoder().createBitmap(matrix))
//    }
//
//}

fun ImageView.setBarCodeImageBitmap(code: String, format: BarcodeFormat = BarcodeFormat.CODE_128) {
    post {
        try {
            val formatter = MultiFormatWriter()
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints.put(EncodeHintType.MARGIN, 0)
            val matrix = formatter.encode(code, format, 800, 200, hints)

            setImageBitmap(BarcodeEncoder().createBitmap(matrix))
        }
        catch (ex: Exception) {
            Timber.e(ex)
        }
    }

}