package com.example.fithit;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class RecommendationModelHandler implements AutoCloseable {

    private final Interpreter interpreter;

    // Constructor: Load the model
    public RecommendationModelHandler(Context context, String modelPath) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, modelPath));
    }

    // Load TFLite model from assets
    private MappedByteBuffer loadModelFile(Context context, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Perform prediction
    public float[] predict(float[] inputData) {
        float[][] input = new float[1][inputData.length]; // 1 sample of N features
        input[0] = inputData;

        // Assuming output is a 1D array with N classes (e.g., 5 recommendation types)
        float[][] output = new float[1][5]; // Adjust size if your model has a different output

        interpreter.run(input, output);

        return output[0]; // Return the first row (prediction scores)
    }

    // Close model resources
    @Override
    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}
