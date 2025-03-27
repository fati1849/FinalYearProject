package com.example.fithit;

import android.content.Context;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class RecommendationModelHandler {

    private Interpreter interpreter;

    // Constructor to load the TFLite model
    public RecommendationModelHandler(Context context, String modelPath) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, modelPath));
    }

    // Function to load the TFLite model from the assets folder
    private MappedByteBuffer loadModelFile(Context context, String modelPath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(context.getAssets().openFd(modelPath).getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = context.getAssets().openFd(modelPath).getStartOffset();
        long declaredLength = context.getAssets().openFd(modelPath).getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Function to run predictions using the model
    public float[] predict(float[] inputData) {
        // Assuming the model has 5 output classes
        float[][] output = new float[1][5];
        interpreter.run(inputData, output);
        return output[0]; // Return the array of probabilities for each class
    }

    // Close the interpreter when done
    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}
