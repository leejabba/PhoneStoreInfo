package kr.heythisway.phonestoreinfo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editStoreCode, editStoreAddress, editStoreFax, editStoreTel, editTeleCompany, editStoreManagerName, editStoreName;
    Button btnSave, btnCancle, btnRead, btnDelete;
    StoreInfo storeInfo;
    DbHelper helper;
    AlertDialog popUp;
    AlertDialog.Builder setPopup;
    List<StoreInfo> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        storeInfo = new StoreInfo();

        helper = DbHelper.getInstance(this);
        datas = helper.readAll();

        findWiget();

        btnSave.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        setPopup = new AlertDialog.Builder(this);
        setPopupWindow("경고", "필수 입력란을 모두 채워주세요!");
    }

    protected void clearEditText() {
        super.onResume();
        editStoreCode.setText("");
        editTeleCompany.setText("");
        editStoreName.setText("");
        editStoreAddress.setText("");
        editStoreTel.setText("");
        editStoreFax.setText("");
        editStoreManagerName.setText("");
    }

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
                }
                // EditText의 값 속성으로 저장
                storeInfo.setStoreCode(editStoreCode.getText().toString());
                storeInfo.setTeleCompanyName(editTeleCompany.getText().toString());
                storeInfo.setStoreName(editStoreName.getText().toString());
                storeInfo.setAddress(editStoreAddress.getText().toString());
                storeInfo.setTel(editStoreTel.getText().toString());
                storeInfo.setFax(editStoreFax.getText().toString());
                storeInfo.setManagerName(editStoreManagerName.getText().toString());
                // 데이터베이스에 입력하기
                helper.create(storeInfo);
                clearEditText();
                break;
            case R.id.btnCancle:
                finish();
                break;
            case R.id.btnRead:
                for (StoreInfo item : datas) {
                    Log.e("저장된 데이터 읽기", "ID : " + item.getId() +
                            " / 매장명 : " + item.getStoreName() +
                            " / 통신사 : " + item.getTeleCompanyName() +
                            " / 주소 : " + item.getAddress() +
                            " / 전화번호 : " + item.getTel() +
                            " / 저장한 날짜 : " + item.getDate());
                }
                break;
            case R.id.btnDelete:
                helper.delete(storeInfo);
                break;
        }
    }
}
