package com.chirikhin.net;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.StrBuilder;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

public class MessageFactory {
    private static final Logger logger = Logger.getLogger(MessageFactory.class.getName());

    private MessageFactory() {

    }

    public static BaseMessage createMessage(byte[] bytes) {
        System.out.println("Length: " +  bytes.length);
        MessageType messageType = MessageType.values()[bytes[0]];
        BaseMessage baseMessage;

        byte[] idByteArray = ArrayUtils.subarray(bytes, 1, 5);
        ByteBuffer byteBuffer = ByteBuffer.wrap(idByteArray);
        int id = byteBuffer.asIntBuffer().get();

        switch (messageType) {
            case BYTE :
                byte[] partBytes = ArrayUtils.subarray(bytes, 5, 9);
                int part = ByteBuffer.wrap(partBytes).asIntBuffer().get();
                System.out.println(part + "");
                bytes = ArrayUtils.subarray(bytes, 9, bytes.length);
                System.out.println(bytes.length + "");
                System.out.println(new String(bytes));
                baseMessage = new ByteMessage(id, part, bytes);
                break;
            case SALUTATION:
                baseMessage = new SalutationMessage(id);
                break;
            case CLOSE:
                baseMessage = new CloseMessage(id);
                break;
            case CONFIRM:
                bytes = ArrayUtils.subarray(bytes, 5, 9);
                ByteBuffer idForConfirmMessageByteBuffer = ByteBuffer.wrap(bytes);
                int idForConfirmMessage = idForConfirmMessageByteBuffer.asIntBuffer().get();
                baseMessage = new ConfirmMessage(id, idForConfirmMessage);
                break;
            default:
                throw new IllegalArgumentException("There is no message that can be constructed from such byte array");
        }

        return baseMessage;
    }
}
