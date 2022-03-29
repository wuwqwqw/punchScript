package com.example.punchscript.Service;

import com.example.punchscript.utils.SeleniumUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.punchscript.constant.PunchEnum;


@Slf4j
@Service
public class HandlePunch {

    @Autowired
    SeleniumUtil seleniumUtil;

    private static final String FORMAT_ONE = "yyyy-MM-dd";

    private static final String FORMAT_TWO = "yyyy-MM-dd hh:mm:ss";

    private static final SimpleDateFormat SDF_ONE = new SimpleDateFormat(FORMAT_ONE);

    private static final SimpleDateFormat SDF_TWO = new SimpleDateFormat(FORMAT_TWO);

    private List<punchEnumTem> punchEnumTemList;

    private void init(){
        punchEnumTemList = Stream.of(PunchEnum.values()).map(stu -> {
            StringBuilder builder = new StringBuilder(stu.body);
            updateTimeStamp(builder);
            return punchEnumTem.builder()
                    .url(stu.url)
                    .body(String.valueOf(builder))
                    .cookie("")
                    .userName(stu.userName)
                    .password(stu.password)
                    .build();
        }).collect(Collectors.toList());
    }

    private void updateTimeStamp(StringBuilder builder) {

        int indexOne = builder.indexOf("\"SBSJ\":\"2022") + 8;//
        int indexTwo = builder.indexOf("\"CLSJ") + 8;
        int indexThree = builder.indexOf("\"SBSJ_STR") + 12;
        int indexFour = builder.indexOf("\"CLSJ_STR") + 12;

        Date now = new Date();
        Date yesterday = new Date(now.getTime() - 24 * 60 * 60 * 1000L);
        builder.replace(indexOne, indexOne + 10, SDF_ONE.format(now));
        builder.replace(indexTwo, indexTwo + 19, SDF_TWO.format(now));
        builder.replace(indexThree, indexThree + 19, SDF_TWO.format(yesterday));
        builder.replace(indexFour, indexFour + 19, SDF_TWO.format(yesterday));
    }

    @Getter
    @Builder
    static class punchEnumTem{
        private String body;
        private String url;
        private String cookie;
        private String userName;
        private String password;
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void handle() throws IOException {
        init();
        for (punchEnumTem tem:punchEnumTemList){
            tem.cookie = seleniumUtil.getCookie(tem.userName,tem.password);
            doHandle(tem);
        }
    }

    private void doHandle(punchEnumTem tem) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse(" text/plain;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, tem.body);
        Request request = new Request.Builder()
                .url(tem.url)
                .method("POST", body)
                .addHeader("Host", " scenter.sdu.edu.cn")
                .addHeader("Connection", " keep-alive")
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
        log.info("code:{},message:您今天已经申请成功过一次，不可继续申请！",response.code());
    }
}
