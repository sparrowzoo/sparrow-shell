package com.sparrow.chat.gpt;

import java.util.concurrent.TimeUnit;
import okhttp3.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ChatGptController {
    private static final String API_KEY = "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";

    private static final String CHATGPT_URL = "https://api.openai.com/v1/chat/completions";
    @GetMapping("/chatgpt")
    public String getChatGptResponse(@RequestParam String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBodyJson = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"Chat with GPT-3.5 Turbo.\"}, {\"role\": \"user\", \"content\": \"%s\"}]}", prompt);
        RequestBody body = RequestBody.create(mediaType, requestBodyJson);
        Request request = new Request.Builder()
            .url(CHATGPT_URL)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + API_KEY)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
