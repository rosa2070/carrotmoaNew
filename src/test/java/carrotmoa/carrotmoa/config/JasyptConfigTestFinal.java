//package carrotmoa.carrotmoa.config;
//
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
//import org.junit.jupiter.api.Test;
//
//public class JasyptConfigTestFinal {
//    @Test
//    public void jasypt_test() {
//        // Jasypt 설정
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//        config.setPassword(""); // 암호화 및 복호화에 사용할 비밀번호
//        config.setAlgorithm("PBEWithMD5AndDES");
//        config.setKeyObtentionIterations("1000");
//        config.setPoolSize("1");
//        config.setProviderName("SunJCE");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
//        config.setStringOutputType("base64");
//        encryptor.setConfig(config);
//
//        // 암호화할 값들
//        String datasourceUrl = "";
//        String username = "";
//        String password = "";
//        String accessKey = "";
//        String secretKey = "";
//
//        String mailHost = "";
//        String mailUsername = "";
//        String mailPassword = "";
//
//        String redisHost = "";
//        String redisUsername = "";
//        String redisPassword = "";
//
//        // 새로운 결제 키 추가
//        String impKey = "";
//        String impSecret = "";
//
//        // 암호화
//        String encryptDatasourceUrl = encryptor.encrypt(datasourceUrl);
//        String encryptUsername = encryptor.encrypt(username);
//        String encryptPassword = encryptor.encrypt(password);
//        String encryptAccessKey = encryptor.encrypt(accessKey);
//        String encryptSecretKey = encryptor.encrypt(secretKey);
//
//        String encryptMailHost = encryptor.encrypt(mailHost);
//        String encryptMailUsername = encryptor.encrypt(mailUsername);
//        String encryptMailPassword = encryptor.encrypt(mailPassword);
//
//        String encryptRedisHost = encryptor.encrypt(redisHost);
//        String encryptRedisUsername = encryptor.encrypt(redisUsername);
//        String encryptRedisPassword = encryptor.encrypt(redisPassword);
//
//        // 결제 키 암호화
//        String encryptImpKey = encryptor.encrypt(impKey);
//        String encryptImpSecret = encryptor.encrypt(impSecret);
//
//        // 암호화 결과 출력
//        System.out.println("Encrypted datasource.url: " + encryptDatasourceUrl);
//        System.out.println("Encrypted spring.datasource.username: " + encryptUsername);
//        System.out.println("Encrypted spring.datasource.password: " + encryptPassword);
//        System.out.println("Encrypted cloud.aws.credentials.accessKey: " + encryptAccessKey);
//        System.out.println("Encrypted cloud.aws.credentials.secretKey: " + encryptSecretKey);
//
//        System.out.println("Encrypted spring.mail.host: " + encryptMailHost);
//        System.out.println("Encrypted spring.mail.username: " + encryptMailUsername);
//        System.out.println("Encrypted spring.mail.password: " + encryptMailPassword);
//
//        System.out.println("Encrypted spring.data.redis.host: " + encryptRedisHost);
//        System.out.println("Encrypted spring.data.redis.username: " + encryptRedisUsername);
//        System.out.println("Encrypted spring.data.redis.password: " + encryptRedisPassword);
//
//        // 암호화된 결제 키 출력
//        System.out.println("Encrypted payment.imp-key: " + encryptImpKey);
//        System.out.println("Encrypted payment.imp-secret: " + encryptImpSecret);
//    }
//}
