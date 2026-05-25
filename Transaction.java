import java.time.LocalDateTime;

// คลาส Model สำหรับเก็บข้อมูลธุรกรรม (Data Payload)
public class Transaction {
    static int counter = 1; // ตัวแปร Static ใช้สร้าง ID อัตโนมัติ (Auto Increment)

    int id; 
    int type; // 1 = ฝาก, 2 = ถอน
    int category; 
    double amount; 
    LocalDateTime datetime; // ประทับเวลาอัตโนมัติ

    // Constructor หลัก
    public Transaction(int type, int category, double amount){
        this.id = counter++; 
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.datetime = LocalDateTime.now(); 
    }

    // Constructor สำรอง (Overloading) สำหรับใช้ในการ Clone ข้อมูล
    public Transaction(int id, int type, int category, double amount){
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.datetime = LocalDateTime.now();
    }

    // เมธอดแปลงรหัสตัวเลขเป็นข้อความประเภท
    public String getTypeName(){
        if(type == 1){
            return "Deposit";
        }
        return "Withdraw";
    }

    // เมธอดแปลงรหัสหมวดหมู่เป็นข้อความที่อ่านง่าย
    public String getCategoryName(){
        switch(category){
            case 1: return "Food";
            case 2: return "Travel";
            case 3: return "Shopping";
            case 4: return "Entertainment";
            case 5: return "Other";
            default: return "-";
        }
    }
}