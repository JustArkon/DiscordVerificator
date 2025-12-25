package net.justempire.discordverificator.services;

import net.justempire.discordverificator.exceptions.InvalidCodeException;
import net.justempire.discordverificator.types.models.UsernameAndIp;
import net.justempire.discordverificator.utils.VerificationCodeGenerator;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationCodeService {
    private final Map<String, UsernameAndIp> codesAndIps;

    public ConfirmationCodeService() {
        codesAndIps = new HashMap<>();
    }

    // Returns code
    public String generateVerificationCode(String username, String ip) {
        String code = VerificationCodeGenerator.generateVerificationCode();
        codesAndIps.put(code.toLowerCase(), new UsernameAndIp(username, ip));
        return code;
    }

    public UsernameAndIp getDataByCodeAndRemove(String code) throws InvalidCodeException {
        UsernameAndIp data = codesAndIps.get(code.toLowerCase());
        if (data == null) throw new InvalidCodeException();
        removeCode(code);
        return data;
    }

    // Remove code with ip
    private void removeCode(String code) {
        codesAndIps.remove(code);
    }
}
