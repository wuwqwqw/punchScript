package com.example.punchscript.Service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.punchscript.constant.punchEnum;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class handlePunch {

    private static final String FORMAT_ONE = "yyyy-MM-dd";

    private static final String FORMAT_TWO = "yyyy-MM-dd hh:mm:ss";

    private static final SimpleDateFormat sdfOne = new SimpleDateFormat(FORMAT_ONE);

    private static final SimpleDateFormat sdfTwo = new SimpleDateFormat(FORMAT_TWO);

    private List<punchEnumTem> punchEnumTemList;

    @PostConstruct
    private void init(){

        List<punchEnumTem> collect = Stream.of(punchEnum.values()).map(stu -> {
            StringBuilder builder = new StringBuilder(stu.body);

            int indexOne = builder.indexOf("\"SBSJ\":\"2022") + 8;//
            int indexTwo = builder.indexOf("\"CLSJ") + 8;
            int indexThree = builder.indexOf("\"SBSJ_STR") + 12;
            int indexFour = builder.indexOf("\"CLSJ_STR") + 12;
            Date now = new Date();
            Date yesterday = new Date(now.getTime() - 24 * 60 * 60 * 1000L);
            builder.replace(indexOne, indexOne + 10, sdfOne.format(now));
            builder.replace(indexTwo, indexTwo + 19, sdfTwo.format(now));
            builder.replace(indexThree, indexThree + 19, sdfTwo.format(yesterday));
            builder.replace(indexFour, indexFour + 19, sdfTwo.format(yesterday));
            String transfer = transfer(String.valueOf(builder));
            return punchEnumTem.builder()
                    .url(stu.url)
                    .contentLength(stu.contentLength)
                    .body(String.valueOf(builder))
                    .cookie(stu.cookie)
                    .build();
        }).collect(Collectors.toList());
        punchEnumTemList = collect;
    }

    private String transfer(String s) {
        Pattern p=Pattern.compile("[A-Za-z]+:");
        Matcher m=p.matcher(s);
        while(m.find()) {
            String group = m.group();
            String groupReplace = "\"" + group.replace(":","\":");
            s = s.replace(group, groupReplace);
        }
        return s.replace("_o","\"_o\"");
    }

    @Builder
    static class punchEnumTem{
        private String body;
        private String url;
        private String contentLength;
        private String cookie;
    }

    @Scheduled(cron = "0 0 0 * * ? ")
    public void doHandlePunch() throws IOException {
        for (punchEnumTem tem:punchEnumTemList){
            handle(tem);
        }
    }

    private void handle(punchEnumTem tem) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse(" text/plain;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, tem.body);
        Request request = new Request.Builder()
                .url(tem.url)
                .method("POST", body)
                .addHeader("Host", " scenter.sdu.edu.cn")
                .addHeader("Connection", " keep-alive")
                .addHeader("Content-Length", tem.contentLength)
                .addHeader("Accept", " application/json, text/javascript, */*; q=0.01")
                .addHeader("X-Requested-With", " XMLHttpRequest")
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat")
                .addHeader("Content-Type", " text/plain;charset=UTF-8")
                .addHeader("Sec-Fetch-Site", " same-origin")
                .addHeader("Sec-Fetch-Mode", " cors")
                .addHeader("Sec-Fetch-Dest", " empty")
                .addHeader("Referer", " https://scenter.sdu.edu.cn/tp_fp/formParser?status=select&formid=d11d0d9b-d73a-4dad-b3c5-7d44b4ed&service_id=41d9ad4a-f681-4872-a400-20a3b606d399&process=674950f5-924b-463d-9eb6-21d5d1b6d9ef&seqId=&seqPid=&privilegeId=7374321285e4d36bbf3d274087454163")
                .addHeader("Accept-Encoding", " gzip, deflate, br")
                .addHeader("Accept-Language", " en-us,en")
                .addHeader("Cookie", tem.cookie)
                .build();
        Response response = client.newCall(request).execute();
        log.info(response.toString());
    }
}
