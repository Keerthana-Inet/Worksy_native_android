
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import java.text.DecimalFormat

public class NumberTextWatcher(val view: View, private val editText: EditText) : TextWatcher {

    private val decimalFormat = DecimalFormat("#,###") // Decimal format with commas
    private val handler = Handler()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Nothing to do
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Nothing to do
    }

    override fun afterTextChanged(s: Editable?) {
        if (s.toString() != "") {
            editText.removeTextChangedListener(this)

            // Remove any existing commas before formatting
            val originalText = (s ?: "0").toString().replace(",", "")

            val formattedText = decimalFormat.format(originalText.toDouble())

            // Update the EditText with the formatted number
            editText.setText(formattedText)

            handler.removeCallbacksAndMessages(null) // Remove any existing callbacks

            editText.setSelection(formattedText.length)

            editText.addTextChangedListener(this)
        }

}

    }





