package net.mamoe.mirai.network.packet.login

import net.mamoe.mirai.network.packet.ServerPacket
import net.mamoe.mirai.network.packet.dataInputStream
import net.mamoe.mirai.network.packet.goto
import net.mamoe.mirai.util.TestedSuccessfully
import net.mamoe.mirai.utils.TEACryptor
import net.mamoe.mirai.utils.hexToUBytes
import java.io.DataInputStream

/**
 * 收到这个包意味着需要验证码登录, 并且能得到验证码图片文件的一半
 *
 * @author Him188moe
 */
class ServerLoginResponseVerificationCodeInitPacket(input: DataInputStream, private val packetLength: Int) : ServerPacket(input) {

    lateinit var verifyCodePart1: ByteArray
    lateinit var token00BA: ByteArray
    var unknownBoolean: Boolean? = null


    @TestedSuccessfully
    @ExperimentalUnsignedTypes
    override fun decode() {
        val verifyCodeLength = this.input.goto(78).readShort()//2bytes
        this.verifyCodePart1 = this.input.readNBytes(verifyCodeLength.toInt())

        this.input.skip(1)

        this.unknownBoolean = this.input.readByte().toInt() == 1

        this.token00BA = this.input.goto(packetLength - 60).readNBytes(40)
    }
}

fun main() {
    val data = "FB 01 04 03 33 00 01 00 BA 02 03 2C 13 00 05 01 00 00 01 23 00 38 D5 01 05 8B 67 4D 52 5A FA 92 DB 99 18 D4 F0 72 03 E0 17 71 7C 8A 45 74 1F C3 2D F8 61 96 0D 93 0D 8C 51 95 70 F8 F9 CB B9 2D 5D BC 4F 5D 89 5F E7 59 8C E4 E5 A2 04 56 02 BC 89 50 4E 47 0D 0A 1A 0A 00 00 00 0D 49 48 44 52 00 00 00 82 00 00 00 35 08 03 00 00 00 BA 12 C3 02 00 00 00 04 67 41 4D 41 00 00 B1 8F 0B FC 61 05 00 00 00 01 73 52 47 42 00 AE CE 1C E9 00 00 00 45 50 4C 54 45 FE F6 ED E2 F1 DF F3 FF F2 11 77 48 FE FE F3 F1 F9 EA D7 FD E7 F8 F9 EC FC EF E7 E8 FF EE 2D 69 48 2A 8A 5D 29 7A 52 F0 ED E1 A9 C7 B1 65 96 79 AB E0 C2 C3 F0 D5 42 7D 5C 4A 99 72 89 AA 93 51 73 5C 6E BA 94 42 BD 7A 0B 00 00 09 C5 49 44 41 54 58 C3 AC 99 8B 76 AB 3A 12 44 91 D0 1B 10 08 04 FF FF A9 B3 5B 60 C7 AF 38 77 EE 0C EB C4 76 6C 07 95 BA AB AB AB 75 BA EE 1F 5D DE CB 63 08 C1 C7 A8 AD BC B6 31 46 6B 3B A3 79 94 E7 F3 63 79 E4 D9 98 89 37 7D F7 FF BB 3C 10 82 6F B7 F7 2A B6 5B 07 EF B5 16 60 DE 1B 63 3A 1F 7E BE 6D 0C 50 EC 09 E1 FE 76 B8 3F FC 6B 0C 1D 5B 37 1F 3E E1 AE 61 9A D8 B6 07 93 B7 56 4D 66 22 06 9D FC EE 5F BF F9 3F 81 90 BB 99 F3 49 E2 1D DA 43 38 13 44 14 8C 9D B4 8E E7 65 40 11 E4 BB FE 8C 5B 78 41 F1 AF 01 48 2E 08 6D 8B AD F1 BA 05 F9 C4 E0 25 E6 31 BA EB 2A 90 C3 74 26 FC 1E 80 CF 14 91 44 7F FC 39 FF D4 37 10 82 C0 B3 B0 67 BF 2E 5E 5B F4 46 E5 3A 5F D7 BE A6 54 17 2D 7C 0D D7 DA E1 7E 93 C7 E5 DB 9E 6E 9F FD A3 14 9C 7F 23 97 55 FA DC 6F 74 8A CC 04 49 03 3F DE 4E 5C 4A 95 9C 53 8D 7A EA 82 F9 10 77 7F EE E5 43 28 C2 9F 00 BA 5B C0 59 A4 CE DB BA AE DB BC 14 D5 2A 82 A5 83 E0 30 92 19 A8 99 36 A7 15 7C BC DF F6 BE D7 86 C0 7F C9 C5 ED 93 C7 1F B9 E0 56 CB AC 61 F5 5D 2E 00 F0 93 D2 5A 9D B6 B0 3F 4B 4D 5C B9 0F C1 EE 29 46 65 4E 40 46 E2 E4 1B 91 C3 A5 2E FE D3 7A 3C 84 0B E3 F3 E2 57 16 2F 08 B9 E6 5A 73 29 65 29 4E B9 25 CF 6B 9A 97 48 20 B2 35 46 1B 6D AD D6 9A 57 69 45 B2 1A 00 90 05 51 8D C7 24 F8 1B B9 FE A6 41 B8 83 6C 59 05 84 9F 44 0F 85 05 9A 97 5A 97 BC A6 6D 81 FE 59 DE 2F 4B 5E E4 DF B2 A4 19 AA 06 D9 FE F9 33 4D 7E 6A 40 FC 97 34 BF 84 E4 81 81 ED E9 DC 85 32 56 47 E5 A4 F0 2D 6F 4D 2A BA 65 4B 73 89 B6 58 5E D7 35 8D 69 E4 4A 6B 76 50 C1 5C 3A D9 59 11 CF 37 99 FA 48 88 70 7F F4 9F 22 12 F2 24 91 3E 2B BF 28 A5 34 68 C0 50 A3 55 DD A4 E3 9C 6E 85 99 95 B6 24 2E 18 D9 3C 5C B1 4D AA 2F 08 E1 75 F1 F0 6B 49 FC BC E3 8D 00 01 00 28 42 E6 18 57 D4 B1 4D AE 51 27 D5 EF A2 38 91 39 15 37 6C 5A FE 75 93 49 DB FC 57 3C 12 3F 26 D9 16 1D 83 45 8B 78 39 D8 01 15 00 10 F6 F0 50 03 74 BB 18 91 D3 55 8D 7F BB 53 15 7A".hexToUBytes().toByteArray();
    ServerLoginResponseVerificationCodeInitPacket(
            data.dataInputStream(),
            data.size
    ).let { it.decode(); println(it) }
}

/*
data
FB 01 04 03 33 00 01 00 BA 02 03 2C 13 00 05 01 00 00 01 23 00 38 D5 01 05 8B 67 4D 52 5A FA 92 DB 99 18 D4 F0 72 03 E0 17 71 7C 8A 45 74 1F C3 2D F8 61 96 0D 93 0D 8C 51 95 70 F8 F9 CB B9 2D 5D BC 4F 5D 89 5F E7 59 8C E4 E5 A2 04 56 02 BC 89 50 4E 47 0D 0A 1A 0A 00 00 00 0D 49 48 44 52 00 00 00 82 00 00 00 35 08 03 00 00 00 BA 12 C3 02 00 00 00 04 67 41 4D 41 00 00 B1 8F 0B FC 61 05 00 00 00 01 73 52 47 42 00 AE CE 1C E9 00 00 00 45 50 4C 54 45 FE F6 ED E2 F1 DF F3 FF F2 11 77 48 FE FE F3 F1 F9 EA D7 FD E7 F8 F9 EC FC EF E7 E8 FF EE 2D 69 48 2A 8A 5D 29 7A 52 F0 ED E1 A9 C7 B1 65 96 79 AB E0 C2 C3 F0 D5 42 7D 5C 4A 99 72 89 AA 93 51 73 5C 6E BA 94 42 BD 7A 0B 00 00 09 C5 49 44 41 54 58 C3 AC 99 8B 76 AB 3A 12 44 91 D0 1B 10 08 04 FF FF A9 B3 5B 60 C7 AF 38 77 EE 0C EB C4 76 6C 07 95 BA AB AB AB 75 BA EE 1F 5D DE CB 63 08 C1 C7 A8 AD BC B6 31 46 6B 3B A3 79 94 E7 F3 63 79 E4 D9 98 89 37 7D F7 FF BB 3C 10 82 6F B7 F7 2A B6 5B 07 EF B5 16 60 DE 1B 63 3A 1F 7E BE 6D 0C 50 EC 09 E1 FE 76 B8 3F FC 6B 0C 1D 5B 37 1F 3E E1 AE 61 9A D8 B6 07 93 B7 56 4D 66 22 06 9D FC EE 5F BF F9 3F 81 90 BB 99 F3 49 E2 1D DA 43 38 13 44 14 8C 9D B4 8E E7 65 40 11 E4 BB FE 8C 5B 78 41 F1 AF 01 48 2E 08 6D 8B AD F1 BA 05 F9 C4 E0 25 E6 31 BA EB 2A 90 C3 74 26 FC 1E 80 CF 14 91 44 7F FC 39 FF D4 37 10 82 C0 B3 B0 67 BF 2E 5E 5B F4 46 E5 3A 5F D7 BE A6 54 17 2D 7C 0D D7 DA E1 7E 93 C7 E5 DB 9E 6E 9F FD A3 14 9C 7F 23 97 55 FA DC 6F 74 8A CC 04 49 03 3F DE 4E 5C 4A 95 9C 53 8D 7A EA 82 F9 10 77 7F EE E5 43 28 C2 9F 00 BA 5B C0 59 A4 CE DB BA AE DB BC 14 D5 2A 82 A5 83 E0 30 92 19 A8 99 36 A7 15 7C BC DF F6 BE D7 86 C0 7F C9 C5 ED 93 C7 1F B9 E0 56 CB AC 61 F5 5D 2E 00 F0 93 D2 5A 9D B6 B0 3F 4B 4D 5C B9 0F C1 EE 29 46 65 4E 40 46 E2 E4 1B 91 C3 A5 2E FE D3 7A 3C 84 0B E3 F3 E2 57 16 2F 08 B9 E6 5A 73 29 65 29 4E B9 25 CF 6B 9A 97 48 20 B2 35 46 1B 6D AD D6 9A 57 69 45 B2 1A 00 90 05 51 8D C7 24 F8 1B B9 FE A6 41 B8 83 6C 59 05 84 9F 44 0F 85 05 9A 97 5A 97 BC A6 6D 81 FE 59 DE 2F 4B 5E E4 DF B2 A4 19 AA 06 D9 FE F9 33 4D 7E 6A 40 FC 97 34 BF 84 E4 81 81 ED E9 DC 85 32 56 47 E5 A4 F0 2D 6F 4D 2A BA 65 4B 73 89 B6 58 5E D7 35 8D 69 E4 4A 6B 76 50 C1 5C 3A D9 59 11 CF 37 99 FA 48 88 70 7F F4 9F 22 12 F2 24 91 3E 2B BF 28 A5 34 68 C0 50 A3 55 DD A4 E3 9C 6E 85 99 95 B6 24 2E 18 D9 3C 5C B1 4D AA 2F 08 E1 75 F1 F0 6B 49 FC BC E3 8D 00 01 00 28 42 E6 18 57 D4 B1 4D AE 51 27 D5 EF A2 38 91 39 15 37 6C 5A FE 75 93 49 DB FC 57 3C 12 3F 26 D9 16 1D 83 45 8B 78 39 D8 01 15 00 10 F6 F0 50 03 74 BB 18 91 D3 55 8D 7F BB 53 15 7A

length 700

verify code
89 50 4E 47 0D 0A 1A 0A 00 00 00 0D 49 48 44 52 00 00 00 82 00 00 00 35 08 03 00 00 00 BA 12 C3 02 00 00 00 04 67 41 4D 41 00 00 B1 8F 0B FC 61 05 00 00 00 01 73 52 47 42 00 AE CE 1C E9 00 00 00 45 50 4C 54 45 FE F6 ED E2 F1 DF F3 FF F2 11 77 48 FE FE F3 F1 F9 EA D7 FD E7 F8 F9 EC FC EF E7 E8 FF EE 2D 69 48 2A 8A 5D 29 7A 52 F0 ED E1 A9 C7 B1 65 96 79 AB E0 C2 C3 F0 D5 42 7D 5C 4A 99 72 89 AA 93 51 73 5C 6E BA 94 42 BD 7A 0B 00 00 09 C5 49 44 41 54 58 C3 AC 99 8B 76 AB 3A 12 44 91 D0 1B 10 08 04 FF FF A9 B3 5B 60 C7 AF 38 77 EE 0C EB C4 76 6C 07 95 BA AB AB AB 75 BA EE 1F 5D DE CB 63 08 C1 C7 A8 AD BC B6 31 46 6B 3B A3 79 94 E7 F3 63 79 E4 D9 98 89 37 7D F7 FF BB 3C 10 82 6F B7 F7 2A B6 5B 07 EF B5 16 60 DE 1B 63 3A 1F 7E BE 6D 0C 50 EC 09 E1 FE 76 B8 3F FC 6B 0C 1D 5B 37 1F 3E E1 AE 61 9A D8 B6 07 93 B7 56 4D 66 22 06 9D FC EE 5F BF F9 3F 81 90 BB 99 F3 49 E2 1D DA 43 38 13 44 14 8C 9D B4 8E E7 65 40 11 E4 BB FE 8C 5B 78 41 F1 AF 01 48 2E 08 6D 8B AD F1 BA 05 F9 C4 E0 25 E6 31 BA EB 2A 90 C3 74 26 FC 1E 80 CF 14 91 44 7F FC 39 FF D4 37 10 82 C0 B3 B0 67 BF 2E 5E 5B F4 46 E5 3A 5F D7 BE A6 54 17 2D 7C 0D D7 DA E1 7E 93 C7 E5 DB 9E 6E 9F FD A3 14 9C 7F 23 97 55 FA DC 6F 74 8A CC 04 49 03 3F DE 4E 5C 4A 95 9C 53 8D 7A EA 82 F9 10 77 7F EE E5 43 28 C2 9F 00 BA 5B C0 59 A4 CE DB BA AE DB BC 14 D5 2A 82 A5 83 E0 30 92 19 A8 99 36 A7 15 7C BC DF F6 BE D7 86 C0 7F C9 C5 ED 93 C7 1F B9 E0 56 CB AC 61 F5 5D 2E 00 F0 93 D2 5A 9D B6 B0 3F 4B 4D 5C B9 0F C1 EE 29 46 65 4E 40 46 E2 E4 1B 91 C3 A5 2E FE D3 7A 3C 84 0B E3 F3 E2 57 16 2F 08 B9 E6 5A 73 29 65 29 4E B9 25 CF 6B 9A 97 48 20 B2 35 46 1B 6D AD D6 9A 57 69 45 B2 1A 00 90 05 51 8D C7 24 F8 1B B9 FE A6 41 B8 83 6C 59 05 84 9F 44 0F 85 05 9A 97 5A 97 BC A6 6D 81 FE 59 DE 2F 4B 5E E4 DF B2 A4 19 AA 06 D9 FE F9 33 4D 7E 6A 40 FC 97 34 BF 84 E4 81 81 ED E9 DC 85 32 56 47 E5 A4 F0 2D 6F 4D 2A BA 65 4B 73 89 B6 58 5E D7 35 8D 69 E4 4A 6B 76 50 C1 5C 3A D9 59 11 CF 37 99 FA 48 88 70 7F F4 9F 22 12 F2 24 91 3E 2B BF 28 A5 34 68 C0 50 A3 55 DD A4 E3 9C 6E 85 99 95 B6 24 2E 18 D9 3C 5C B1 4D AA 2F 08 E1 75 F1 F0 6B 49 FC BC E3 8D

token00ba
42 E6 18 57 D4 B1 4D AE 51 27 D5 EF A2 38 91 39 15 37 6C 5A FE 75 93 49 DB FC 57 3C 12 3F 26 D9 16 1D 83 45 8B 78 39 D8
 */

class ServerLoginResponseVerificationCodePacketEncrypted(input: DataInputStream) : ServerPacket(input) {
    override fun decode() {

    }

    fun decrypt(): ServerLoginResponseVerificationCodeInitPacket {
        this.input goto 14
        val data = TEACryptor.CRYPTOR_SHARE_KEY.decrypt(this.input.readAllBytes().let { it.copyOfRange(0, it.size - 1) });
        return ServerLoginResponseVerificationCodeInitPacket(data.dataInputStream(), data.size)
    }
}