package edu.gatech.seclass.sdpencryptor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText sourceText, slopeInput, offsetInput;
    private TextView transformedText;
    private Button transformButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Elements
        sourceText = findViewById(R.id.sourceTextID);
        slopeInput = findViewById(R.id.slopeInputID);
        offsetInput = findViewById(R.id.offsetInputID);
        transformedText = findViewById(R.id.transformedTextID);
        transformButton = findViewById(R.id.transformButtonID);

        // Button Click Listener
        transformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transformText();
            }
        });
    }

    private void transformText() {
        String inputText = sourceText.getText().toString();
        String slopeStr = slopeInput.getText().toString();
        String offsetStr = offsetInput.getText().toString();

        // Input Validation
        if (inputText.isEmpty() || !inputText.matches(".*[a-zA-Z0-9].*")) {
            sourceText.setError("Invalid Source Text");
            return;
        }

        if (slopeStr.isEmpty() || !isValidSlope(Integer.parseInt(slopeStr))) {
            slopeInput.setError("Invalid Slope Input");
            return;
        }

        if (offsetStr.isEmpty() || Integer.parseInt(offsetStr) < 1 || Integer.parseInt(offsetStr) > 61) {
            offsetInput.setError("Invalid Offset Input");
            return;
        }

        int slope = Integer.parseInt(slopeStr);
        int offset = Integer.parseInt(offsetStr);

        // Encrypting Text
        String encryptedText = encryptAffineCipher(inputText, slope, offset);
        transformedText.setText(encryptedText);
    }

    private boolean isValidSlope(int slope) {
        // Check if slope is coprime to 62
        return (slope > 0 && slope < 62 && gcd(slope, 62) == 1);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private String encryptAffineCipher(String text, int slope, int offset) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder encrypted = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (alphabet.indexOf(c) != -1) {
                int originalValue = alphabet.indexOf(c);
                int encryptedValue = (originalValue * slope + offset) % 62;
                encrypted.append(alphabet.charAt(encryptedValue));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
}
