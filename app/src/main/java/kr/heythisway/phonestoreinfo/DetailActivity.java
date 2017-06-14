package kr.heythisway.phonestoreinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    // 팝업창 용도에 따른 상수 설정
    final static int POPUP_NECESSARY = 1;
    final static int POPUP_DELETE = 2;

    // 위젯 변수
    EditText editStoreCode, editStoreAddress, editStoreFax, editStoreTel,
            editTeleCompany, editStoreManagerName, editStoreName;
    Button btnSave, btnCancle, btnDelete, btnUpdate;
    ImageButton btnOpenMap;

    // 데이터 보관용 변수
    StoreInfo storeInfo;
    TempStroeInfo tempStroeInfo;
    List<StoreInfo> datas;
    DbHelper helper;

    // 경고 다이얼로그 변수
    AlertDialog popUp;
    AlertDialog.Builder setPopup;

    // Intent 값 받을 변수
    Bundle bundle;
    int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 위젯을 찾는다.
        findWiget();

        storeInfo = new StoreInfo();
        tempStroeInfo = new TempStroeInfo();

        helper = DbHelper.getInstance(this);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            storeId = intent.getIntExtra("id", 0);
            //Toast.makeText(this, "받아온 ID : " + storeId, Toast.LENGTH_SHORT).show();
            // 값을 불러와 EditText에 할당하는 메서드 호출
            setDetailValue(storeId);
        }

        // 처음 액티비티 onCreate 상태일때 EditText 상태를 저장한다.
        tempDB();
        // 필수입력 항목인 매장코드란이 비어있지 않으면 저장되어 있는 상세정보이므로 '업데이트'버튼을 보여준다.
        if (!editStoreCode.getText().toString().equals("")) {
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        }

        callButtonClickLisener();

        setPopup = new AlertDialog.Builder(this);
    }

    // MainActiviy listView에서 선택된 아이템 기준으로 불러온 데이터 값을 화면에 뿌려주는 메서드
    private void setDetailValue(int storeId) {
        storeInfo = helper.read(storeId);
        editStoreCode.setText(storeInfo.getStoreCode());
        editTeleCompany.setText(storeInfo.getTeleCompanyName());
        editStoreName.setText(storeInfo.getStoreName());
        editStoreAddress.setText(storeInfo.getAddress());
        editStoreTel.setText(storeInfo.getTel());
        editStoreFax.setText(storeInfo.getFax());
        editStoreManagerName.setText(storeInfo.getManagerName());
//        bundle.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callButtonClickLisener() {
        btnSave.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnOpenMap.setOnClickListener(this);
    }

    protected void clearEditText() {
        // EditText 내용 모두 지우고
        editStoreCode.setText("");
        editTeleCompany.setText("");
        editStoreName.setText("");
        editStoreAddress.setText("");
        editStoreTel.setText("");
        editStoreFax.setText("");
        editStoreManagerName.setText("");
        // 임시값 초기화
        tempDB();
    }


    /**
     * 팝업창 설정 메서드
     *
     * @param title 팝업창 제목
     * @param msg   팝업창 내용
     * @param order 팝업 목적 - 상수 설정 값 POPUP_NECESSARY : 필수항목 입력요청 팝업 / POPUP_DELETE : 삭제여부 확인
     */
    private void setPopupWindow(String title, String msg, int order) {
        setPopup.setTitle(title);
        setPopup.setMessage(msg);
        switch (order) {
            case POPUP_NECESSARY:
                setPopup.setPositiveButton("확인", null);
                break;
            case POPUP_DELETE:
                setPopup.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearEditText();
                        helper.delete(storeInfo.getId());
                        Toast.makeText(DetailActivity.this, storeInfo.getStoreCode() + " 매장을 삭제하셨습니다. ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("취소", null);
                break;
        }
        popUp = setPopup.create();
    }

    /**
     * 위젯뷰 findViewById 호출
     */
    private void findWiget() {
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancle = (Button) findViewById(R.id.btnCancle);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.GONE);
        btnOpenMap = (ImageButton) findViewById(R.id.btnOpenMap);

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
                    setPopupWindow("경고", "필수 입력란을 모두 채워주세요!", POPUP_NECESSARY);
                    popUp.show();
                } else {
                    // EditText의 값 Temp 스토리지에 임시저장
                    tempDB();
                    // EditText의 값 DB에 저장
                    insertDB();
                    Toast.makeText(this, storeInfo.getStoreCode() + " 매장 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    btnSave.setVisibility(View.GONE);
                    btnUpdate.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnCancle:
                finish();
                break;
            case R.id.btnUpdate:
                // tempDB 값과 EditText의 값의 변화를 체크하는 메서드 호출
                compareValue();
                break;
            case R.id.btnDelete:
                if (!editStoreCode.getText().toString().equals("")) {
                    setPopupWindow("경고", "삭제하면 되돌릴 수 없습니다!", POPUP_DELETE);
                    popUp.show();
                } else {
                    Toast.makeText(this, "지금은 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnOpenMap:
                Uri uri = Uri.parse("geo:37.7749,-122.4194?z=13?q=" + Uri.encode("1st & Pike, Seattle"));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
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
            Toast.makeText(this, storeInfo.getStoreCode() + " 매장 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
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
