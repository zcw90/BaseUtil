package com.zcw.baseutildemo.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by 朱城委 on 2019/8/19.<br><br>
 */
public class CertificateDemo {
    public static void main(String[] args) throws CertificateException, FileNotFoundException {
        CertificateDemo demo = new CertificateDemo();
        demo.readCertificateInfo("./baidu_com.crt");
    }

    /**
     * 读取证书信息
     * @param fileName 证书文件名
     */
    private void readCertificateInfo(String fileName) throws CertificateException, FileNotFoundException {
        File file = new File(fileName);
        if(!file.exists()) {
            System.out.println(fileName + "证书不存在");
            return;
        }

        FileInputStream inputStream = new FileInputStream(file);
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        Certificate certificate = factory.generateCertificate(inputStream);

        if(certificate instanceof X509Certificate) {
            X509Certificate x509Certificate = (X509Certificate)certificate;
            System.out.println("SigAlgName: " + x509Certificate.getSigAlgName());
            System.out.println("SigAlgOID: " + x509Certificate.getSigAlgOID());
            System.out.println("BasicConstraints: " + x509Certificate.getBasicConstraints());
            System.out.println("ExtendedKeyUsage: " + x509Certificate.getExtendedKeyUsage());
            System.out.println("IssuerAlternativeNames: " + x509Certificate.getIssuerAlternativeNames());
            System.out.println("IssuerDN: " + x509Certificate.getIssuerDN());
            System.out.println("IssuerUniqueID: " + x509Certificate.getIssuerUniqueID());
            System.out.println("IssuerX500Principal: " + x509Certificate.getIssuerX500Principal());
            System.out.println("KeyUsage: " + x509Certificate.getKeyUsage());
            System.out.println("NotAfter: " + x509Certificate.getNotAfter());
            System.out.println("NotBefore: " + x509Certificate.getNotBefore());
            System.out.println("SerialNumber: " + x509Certificate.getSerialNumber());
            System.out.println("SigAlgParams: " + x509Certificate.getSigAlgParams());
            System.out.println("Signature: " + x509Certificate.getSignature());
            System.out.println("SubjectAlternativeNames: " + x509Certificate.getSubjectAlternativeNames());
            System.out.println("SubjectDN: " + x509Certificate.getSubjectDN());
            System.out.println("SubjectUniqueID: " + x509Certificate.getSubjectUniqueID());
            System.out.println("SubjectX500Principal: " + x509Certificate.getSubjectX500Principal());
            System.out.println("Type: " + x509Certificate.getType());
            System.out.println("TBSCertificate: " + x509Certificate.getTBSCertificate());
            System.out.println("PublicKey: " + x509Certificate.getPublicKey());
            System.out.println("Version: " + x509Certificate.getVersion());
        }
        else {
            System.out.println(fileName + "证书不是X509格式证书");
        }
    }
}
