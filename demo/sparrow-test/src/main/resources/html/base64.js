;(function(global) {
    //int转byte
    function int2Byte(i) {
        var b = i & 0xFF; //[0-255]
        var c = 0;
        if (b >= 0x80) {
            c = b % 0x80;
            c = -1 * (0x80 - c);
        } else {
            c = b;
        }
        return c;
    }
    //字符串转byte[]
    function str2Bytes(str) {
        var ch, stack, result = [];
        for (var i = 0, len = str.length; i < len; i++) {
            ch = str.charCodeAt(i);
            stack = [];
            if (ch >= 0x010000 && ch <= 0x10FFFF) {
                stack.push(int2Byte(((ch >> 18) & 0x07) | 0xF0));
                stack.push(int2Byte(((ch >> 12) & 0x3F) | 0x80));
                stack.push(int2Byte(((ch >> 6) & 0x3F) | 0x80));
                stack.push(int2Byte((ch & 0x3F) | 0x80));
            } else if (ch >= 0x000800 && ch <= 0x00FFFF) {
                stack.push(int2Byte(((ch >> 12) & 0x0F) | 0xE0));
                stack.push(int2Byte(((ch >> 6) & 0x3F) | 0x80));
                stack.push(int2Byte((ch & 0x3F) | 0x80));
            } else if (ch >= 0x000080 && ch <= 0x0007FF) {
                stack.push(int2Byte(((ch >> 6) & 0x1F) | 0xC0));
                stack.push(int2Byte((ch & 0x3F) | 0x80));
            } else {
                stack.push(ch & 0xFF);
            }
            result = result.concat(stack);
        }
        return result;
    }
    //byte[]转string
    function bytes2Str(_arr) {
        var unicodeStr = '';
        for (var pos = 0, len = _arr.length; pos < len;) {
            var flag = _arr[pos];
            var unicode = 0;
            if ((flag >>> 7) === 0) {
                unicodeStr += String.fromCharCode(_arr[pos]);
                pos += 1;
            } else if ((flag & 0xF0) === 0xF0) {
                unicode = (_arr[pos] & 0xF) << 18;
                unicode |= (_arr[pos + 1] & 0x3F) << 12;
                unicode |= (_arr[pos + 2] & 0x3F) << 6;
                unicode |= (_arr[pos + 3] & 0x3F);
                unicodeStr += String.fromCodePoint(unicode);
                pos += 4;
            } else if ((flag & 0xE0) === 0xE0) {
                unicode = (_arr[pos] & 0x1F) << 12;;
                unicode |= (_arr[pos + 1] & 0x3F) << 6;
                unicode |= (_arr[pos + 2] & 0x3F);
                unicodeStr += String.fromCharCode(unicode);
                pos += 3;
            } else if ((flag & 0xC0) === 0xC0) {
                unicode = (_arr[pos] & 0x3F) << 6;
                unicode |= (_arr[pos + 1] & 0x3F);
                unicodeStr += String.fromCharCode(unicode);
                pos += 2;
            } else {
                unicodeStr += String.fromCharCode(_arr[pos]);
                pos += 1;
            }
        }
        return unicodeStr;
    }
    var toBase64 = [
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    ];
    var fromBase64 = new Array();
    for (var i = 0, len = 256; i < len; i++) {
        fromBase64[i] = -1;
    }
    for (var j = 0; j < toBase64.length; j++) {
        fromBase64[toBase64[j].charCodeAt(0)] = j;
    }
    fromBase64['='.charCodeAt(0)] = -2;
    //base64编码 string return byte[]
    function _encodeBase64(src) {
        src = src || '';
        var bsrc = str2Bytes(src);
        var end = bsrc.length;
        var off = 0;
        var dst = [];
        var base64 = [];
        for (var i = 0, len = toBase64.length; i < len; i++) {
            base64[i] = toBase64[i].charCodeAt(0);
        }
        var sp = off;
        var slen = Math.trunc((end - off) / 3) * 3;
        var sl = off + slen;
        var dp = 0;
        while (sp < sl) {
            var sl0 = Math.min(sp + slen, sl);
            for (var sp0 = sp, dp0 = dp; sp0 < sl0;) {
                var bits = (bsrc[sp0++] & 0xff) << 16 | (bsrc[sp0++] & 0xff) << 8 | (bsrc[sp0++] & 0xff);
                dst[dp0++] = int2Byte(base64[(bits >>> 18) & 0x3f]);
                dst[dp0++] = int2Byte(base64[(bits >>> 12) & 0x3f]);
                dst[dp0++] = int2Byte(base64[(bits >>> 6) & 0x3f]);
                dst[dp0++] = int2Byte(base64[bits & 0x3f]);
            }
            var dlen = (sl0 - sp) / 3 * 4;
            dp += dlen;
            sp = sl0;
        }
        if (sp < end) {
            var b0 = bsrc[sp++] & 0xff;
            dst[dp++] = int2Byte(base64[b0 >> 2]);
            if (sp == end) {
                dst[dp++] = int2Byte(base64[(b0 << 4) & 0x3f]);
                dst[dp++] = '='.charCodeAt(0);
                dst[dp++] = '='.charCodeAt(0);
            } else {
                var b1 = bsrc[sp++] & 0xff;
                dst[dp++] = int2Byte(base64[(b0 << 4) & 0x3f | (b1 >> 4)]);
                dst[dp++] = int2Byte(base64[(b1 << 2) & 0x3f]);
                dst[dp++] = '='.charCodeAt(0);
            }
        }
        return dst;
    }
    //base64解码 string return byte[]
    function _decodeBase64(src) {
        src = (src || '').replace(/[^A-z0-9+/]/g, '');
        var bsrc = str2Bytes(src);
        var sl = bsrc.length;
        var sp = 0;
        var dst = [];
        var base64 = fromBase64;
        var dp = 0;
        var bits = 0;
        var shiftto = 18;
        while (sl > sp) {
            var b = bsrc[sp++] & 0xff;
            if ((b = base64[b]) < 0) {
                if (b == -2) {
                    break;
                }
            }
            bits |= (b << shiftto);
            shiftto -= 6;
            if (shiftto < 0) {
                dst[dp++] = int2Byte(bits >> 16);
                dst[dp++] = int2Byte(bits >> 8);
                dst[dp++] = int2Byte(bits);
                shiftto = 18;
                bits = 0;
            }
        }
        if (shiftto == 6) {
            dst[dp++] = int2Byte(bits >> 16);
        } else if (shiftto == 0) {
            dst[dp++] = int2Byte(bits >> 16);
            dst[dp++] = int2Byte(bits >> 8);
        }
        return dst;
    }

    global.BASE64 = {
        decodeBase64: _decodeBase64,
        encodeBase64: _encodeBase64,
        strToBytes: str2Bytes,
        bytesToString: bytes2Str
    };
})(window);
