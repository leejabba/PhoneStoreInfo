package kr.heythisway.phonestoreinfo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    // 위젯 변수
    EditText editStoreCode, editStoreAddress, editStoreFax, editStoreTel, editTeleCompany, editStoreManagerName, editStoreName;
    Button btnSave, btnCancle, btnRead, btnDelete, btnUpdate;

    // 데이터 보관용 변수
    StoreInfo storeInfo;
    TempStroeInfo tempStroeInfo;
    List<StoreInfo> datas;
    DbHelper helper;

    // 경고 다이얼로그 변수
    AlertDialog popUp;
    AlertDialog.Builder setPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 위젯을 찾는다.
        findWiget();

        storeInfo = new StoreInfo();
        tempStroeInfo = new TempStroeInfo();

        helper = DbHelper.getInstance(this);
        datas = helper.readAll();

        // 처음 액티비티 onCreate 상태일때 EditText 상태를 저장한다.
        tempDB();
        // 필수입력 항목인 매장코드란이 비어있지 않으면 저장되어 있는 상세정보이므로 '업데이트'버튼을 보여준다.
        if (!editStoreCode.getText().toString().equals("")) {
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }

        buttonClickLisener();

        setPopup = new AlertDialog.Builder(this);
        setPopupWindow("경고", "필수 입력란을 모두 채워주세요!");
    }

    private void buttonClickLisener() {
        btnSave.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

//    protected void clearEditText() {
//        editStoreCode.setText("");
//        editTeleCompany.setText("");
//        editStoreName.setText("");
//        editStoreAddress.setText("");
//        editStoreTel.setText("");
//        editStoreFax.setText("");
//        editStoreManagerName.setText("");
//    }

    /**
     * 팝업창 설정 메서드
     *
     * @param title 팝업창 제목
     * @param msg   팝업창 내용
     */
    private void setPopupWindow(String title, String msg) {
        setPopup.setTitle(title);
        setPopup.setMessage(msg);
        setPopup.setPositiveButton("확인", null);
        popUp = setPopup.create();
    }

    /**
     * 위젯뷰 findViewById 호출
     */
    private void findWiget() {
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancle = (Button) findViewById(R.id.btnCancle);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.GONE);

        editStoreCode = (EditText) findViewById(R.id.editStoreCode);
        editTeleCompany = (EditText) findViewById(R.id.editTeleCompany);
        editStoreName = (EditText) findViewById(R.id.editStoreName);
        editStoreAddress = (EditText) findViewById(R.id.editStoreAddress);
        editStoreTel = (EditText) findViewById(R.id.editStoreTel);
        editStoreFax = (EditText) findViewById(R.id.editStoreFax);
        editStoreManagerName = (EditText) findViewById(R.id.editStoreManagerName);
    }

    // 물리적 Back 버튼을 적용시키지 않기위해서는 onBackPressed 메서드의 super를 삭제하면 된다.
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (editStoreCode.getText().toString().equals("") || editStoreName.getText().toString().equals("")
                        || editStoreAddress.getText().toString().equals("") || editStoreTel.getText().toString().equals("")) {
                    popUp.show();
                } else {
                    // EditText의 값 Temp 스토리지에 임시저장
                    tempDB();
                    // EditText의 값 DB에 저장
                    insertDB();
                    btnSave.setVisibility(View.GONE);
                    btnUpdate.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnCancle:
                finish();
                break;
            case R.id.btnRead:
                readData();
                break;
            case R.id.btnUpdate:
                // tempDB 값과 EditText의 값의 변화를 체크하는 메서드 호출
                compareValue();
                break;
            case R.id.btnDelete:
                Toast.makeText(this, "본 매장의 id는 " + storeInfo.getId() + "입니다.", Toast.LENGTH_SHORT).show();
//                helper.delete(storeInfo.getId());
                break;
        }
    }

    // 값 업데이트를 위한 값 비교 메서드
    private void compareValue() {
        if (!tempStroeInfo.storeCode.equals(editStoreCode.getText().toString()) ||
                !tempStroeInfo.teleCompanyName.equals(editTeleCompany.getText().toString()) ||
                !tempStroeInfo.storeName.equals(editStoreName.getText().toString()) ||
                !tempStroeInfo.address.equals(editStoreAddress.getText().toString()) ||
                !tempStroeInfo.tel.equals(editStoreTel.getText().toString()) ||
                !tempStroeInfo.fax.equals(editStoreFax.getText().toString()) ||
                !tempStroeInfo.managerName.equals(editStoreManagerName.getText().toString())) {
            // 값을 업데이트
            storeInfo = helper.read(storeInfo.getId());
            storeInfo.setStoreCode(editStoreCode.getText().toString());
            storeInfo.setTeleCompanyName(editTeleCompany.getText().toString());
            storeInfo.setStoreName(editStoreName.getText().toString());
            storeInfo.setAddress(editStoreAddress.getText().toString());
            storeInfo.setTel(editStoreTel.getText().toString());
            storeInfo.setFax(editStoreFax.getText().toString());
            storeInfo.setManagerName(editStoreManagerName.getText().toString());
            helper.update(storeInfo);
        }
    }

    private void insertDB() {
        storeInfo.setStoreCode(tempStroeInfo.storeCode);
        storeInfo.setTeleCompanyName(tempStroeInfo.teleCompanyName);
        storeInfo.setStoreName(tempStroeInfo.storeName);
        storeInfo.setAddress(tempStroeInfo.address);
        storeInfo.setTel(tempStroeInfo.tel);
        storeInfo.setFax(tempStroeInfo.fax);
        storeInfo.setManagerName(tempStroeInfo.managerName);
        // 데이터베이스에 입력하기
        helper.create(storeInfo);
    }

    // 임시값 저장 메서드
    private void tempDB() {
        tempStroeInfo.storeCode = editStoreCode.getText().toString();
        tempStroeInfo.teleCompanyName = editTeleCompany.getText().toString();
        tempStroeInfo.storeName = editStoreName.getText().toString();
        tempStroeInfo.address = editStoreAddress.getText().toString();
        tempStroeInfo.tel = editStoreTel.getText().toString();
        tempStroeInfo.fax = editStoreFax.getText().toString();
        tempStroeInfo.managerName = editStoreManagerName.getText().toString();
    }

    private void readData() {
        for (StoreInfo item : datas) {
            Log.e("저장된 데이터 읽기", "ID : " + item.getId() +
                    " / 매장코드 : " + item.getStoreCode() +
                    " / 매장명 : " + item.getStoreName() +
                    " / 통신사 : " + item.getTeleCompanyName() +
                    " / 주소 : " + item.getAddress() +
                    " / 전화번호 : " + item.getTel() +
                    " / 저장한 날짜 : " + item.getDate());
        }
    }
}


