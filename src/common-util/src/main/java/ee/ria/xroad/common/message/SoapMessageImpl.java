/**
 * The MIT License
 * Copyright (c) 2015 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ee.ria.xroad.common.message;

import ee.ria.xroad.common.identifier.CentralServiceId;
import ee.ria.xroad.common.identifier.ClientId;
import ee.ria.xroad.common.identifier.SecurityServerId;
import ee.ria.xroad.common.identifier.ServiceId;

import lombok.SneakyThrows;

import javax.xml.soap.SOAPMessage;

import static ee.ria.xroad.common.message.SoapUtils.isResponseMessage;
import static ee.ria.xroad.common.util.CryptoUtils.calculateDigest;

/**
 * This class represents the X-Road SOAP message.
 */
public class SoapMessageImpl extends AbstractSoapMessage<SoapHeader> {

    private byte[] hash;

    SoapMessageImpl(byte[] rawXml, String charset, SoapHeader header,
            SOAPMessage soap, String serviceName, boolean isRpcEncoded,
            String originalContentType) throws Exception {
        super(rawXml, charset, header, soap, isResponseMessage(serviceName),
                isRpcEncoded, originalContentType);
    }

    /**
     * Lazy method to retrieve the hash of the message, will calculate it
     * on the first invocation of the method.
     * @return hash of the message
     */
    @SneakyThrows
    public byte[] getHash() {
        if (hash == null) {
            hash = calculateDigest(SoapUtils.getHashAlgoId(), getBytes());
        }
        return hash;
    }

    /**
     * Gets the client ID in the SOAP message header.
     *
     * @return ClientId
     */
    public ClientId getClient() {
        return getHeader().getClient();
    }

    /**
     * Gets the service ID in the SOAP message header.
     *
     * @return ServiceId
     */
    public ServiceId getService() {
        return getHeader().getService();
    }

    /**
     * Gets the central service ID in the SOAP message header.
     *
     * @return CentralServiceId
     */
    public CentralServiceId getCentralService() {
        return getHeader().getCentralService();
    }

    /**
     * Gets the security server ID in the SOAP message header.
     * @return SecurityServerId
     */
    public SecurityServerId getSecurityServer() {
        return getHeader().getSecurityServer();
    }

    /**
     * Gets the query ID from the SOAP message header.
     *
     * @return String
     */
    public String getQueryId() {
        return getHeader().getQueryId();
    }

    /**
     * Gets the user ID from the SOAP message header.
     *
     * @return String
     */
    public String getUserId() {
        return getHeader().getUserId();
    }

    /**
     * Gets the represented party from the SOAP message header.
     * @return RepresentedParty
     */
    public RepresentedParty getRepresentedParty() {
        return getHeader().getRepresentedParty();
    }

    /**
     * Gets the issue from the SOAP message header.
     * @return String
     */
    public String getIssue() {
        return getHeader().getIssue();
    }

    /**
     * Gets the protocol version from the SOAP message header.
     * @return String
     */
    public String getProtocolVersion() {
        return getHeader().getProtocolVersion().getVersion();
    }

}

