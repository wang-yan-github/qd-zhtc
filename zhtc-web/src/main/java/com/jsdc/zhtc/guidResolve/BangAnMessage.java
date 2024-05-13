package com.jsdc.zhtc.guidResolve;

import lombok.Data;

@Data
public class BangAnMessage {
    private String magic;
    private String frameLength;
    private String protocolVersion;
    private String protocolType;
    private String encryptionType;
    private String encryptionSeed;
    private String textLength;
    private String binaryLength;
    private String reserved;
    private String messageSequence;
    private String dataCheckSum;
    private String headerCheckSum;
    private String data;

    public String toString() {
        String frontData = this.getMagic() + this.getFrameLength()
                + this.getProtocolVersion() + this.getProtocolType() + this.getEncryptionType()
                + this.getEncryptionSeed() + this.getTextLength() + this.getBinaryLength()
                + this.getReserved() + this.getMessageSequence() + this.getDataCheckSum()
                + this.getHeaderCheckSum();
        return frontData;
    }
}
