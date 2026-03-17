package com.academo.util.config.storage;

import com.academo.model.User;
import com.academo.util.exceptions.fileTransfer.FileSizeException;
import com.academo.util.exceptions.fileTransfer.MimeTypeException;
import com.academo.util.exceptions.fileTransfer.UserStorageIsFullException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class FileValidation {

    private static final long ONE_MB = 1024L * 1024L;
    private static final long FIFTEEN_MB = 15 * ONE_MB;
    private static final long THREE_HUNDRED_MB = 300 * ONE_MB;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/csv",
            "text/plain"
    );


    public static Boolean isMimeTypeValid(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new MimeTypeException("Tipo de arquivo não permitido");
        }
        return true;
    }

    public static Boolean isFileSizeValid(MultipartFile file) {
        if(file.getSize() > FIFTEEN_MB) throw new FileSizeException("O tamanho do arquivo ultrapassou o limite permitido");
        return true;
    }

    public static Boolean isUserStorageFull(MultipartFile file, User user) {
        if(user.getStorageUsage() + file.getSize() > THREE_HUNDRED_MB) {
            throw new UserStorageIsFullException("O limite de armazenamento foi atingido");
        }
        return true;
    }
}
