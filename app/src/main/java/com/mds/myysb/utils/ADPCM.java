package com.mds.myysb.utils;

public class ADPCM {

    private static final int index_adjust[] = {-1, -1, -1, -1, 2, 4, 6, 8,-1, -1, -1, -1, 2, 4, 6, 8};

    private static final int step_table[] = {7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 21, 23, 25, 28, 31,
            34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130,
            143, 157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449,
            494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552,
            1707, 1878, 2066, 2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871,
            5358, 5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899, 15289,
            16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767};


    public static byte[] decodeAdpcm(byte[] in){
        int adpcm_index;
        int adpcm_value;
        int step;
        int vpdiff;
        byte codeData;  //unsigned char,是否一定是unsigned字符
        byte[] out = new byte[200];
        int i,j,k;
        int sign,delta;

        int dat = 0;


        int l_value = in[102]&0xff;
        int h_value = in[103]&0xff;
        adpcm_value = (h_value<<8) + l_value;

//		adpcm_value = Integer.parseInt(String.format("%02X%02X", in[103],in[102]),16);

        adpcm_index = in[101]&0xff;//

        //android.util.Log.e("ADPCM", String.format("--%08X,%d,%02X--",adpcm_value,adpcm_value,adpcm_index));
        if(adpcm_index<0){
            adpcm_index=0;
        }
        if(adpcm_index>88){
            adpcm_index=88;
        }
        step = step_table[adpcm_index];

        for(i=0;i<100;i++){//每个包中有100个字节的声音数据
            k = (i<<1);//k=i*2
            codeData = in[i];//得到下一个数据
            for(j=0;j<2;j++){//将一个字节的数据中的高4位和低4位分别转换成两个字节数据
                if(j==0){
                    delta = (codeData >> 4) & 0xf;
                }else{
                    delta = (codeData >> 0) & 0xf;
                }

                adpcm_index += index_adjust[delta];
                if(adpcm_index<0){
                    adpcm_index=0;
                }
                if(adpcm_index>88){
                    adpcm_index=88;
                }
                sign = delta & 8;
                delta = delta & 7;

                vpdiff = step >> 3;
                if((delta & 4) != 0 ){
                    vpdiff += step;
                }
                if((delta & 2) != 0 ){
                    vpdiff += step>>1;
                }
                if((delta & 1) != 0 ){
                    vpdiff += step>>2;
                }

                if (0 != sign){
                    adpcm_value -= vpdiff;
                    if(adpcm_value < -32768){
                        adpcm_value = -32768;
                    }
                }else{
                    adpcm_value += vpdiff;
                    if(adpcm_value > 32767){
                        adpcm_value = 32767;
                    }
                }
                step = step_table[adpcm_index];

                //adpcm_value *= 200;

                dat = adpcm_value;

                if(dat > 0x3FF){
                    dat = 0x3FF;
                }
                if(dat < 0){
                    dat = 0;
                }
                dat >>= 2;

                out[k+j] = (byte)dat;//总共接收到200个数据
            }
        }
        return out;
    }

}
