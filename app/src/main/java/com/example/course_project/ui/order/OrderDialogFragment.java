package com.example.course_project.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.akexorcist.snaptimepicker.SnapTimePickerDialog;
import com.akexorcist.snaptimepicker.TimeRange;
import com.akexorcist.snaptimepicker.TimeValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.course_project.R;
import com.example.course_project.data.db.cart.CartDAO_Impl;
import com.example.course_project.data.db.cart.CartDataSource;
import com.example.course_project.data.db.cart.CartDatabase;
import com.example.course_project.data.db.cart.LocalCartDataSource;
import com.example.course_project.data.db.login.LoginDataSource;
import com.example.course_project.data.db.login.LoginRepository;
import com.example.course_project.dto.OrderDto;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Calendar;

public class OrderDialogFragment extends BottomSheetDialogFragment {
    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
    private final CartDataSource cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_making, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view.findViewById(R.id.choose_time).setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            SnapTimePickerDialog timePicker = new SnapTimePickerDialog.Builder()
                    .setPreselectedTime(new TimeValue(hour, minute))
                    .setSelectableTimeRange(new TimeRange(new TimeValue(9, 30), new TimeValue(18, 0)))
                    .setTitle(R.string.order_time_title)
                    .setThemeColor(R.color.colorAccent)
                    .build();
            timePicker.setListener((h, m) -> {
                LocalTime orderTime = LocalTime.of(h, m);
                ((TextView) view.findViewById(R.id.order_time)).setText("Подготовить заказ к " + orderTime);
                // TODO: добавить время в OrderDto
            });
            timePicker.show(requireActivity().getSupportFragmentManager(), SnapTimePickerDialog.TAG);
        });

        OrderDto orderDto = new OrderDto();
        orderDto.setBuildingName("Ломо");
        orderDto.setId(20L);
        orderDto.setMonitorCode(loginRepository.getLoggedUser().getUserId() + "_" + loginRepository.getLoggedUser().getDisplayName() + ":mon");
        System.out.println(cartDataSource.getAllCart(loginRepository.getLoggedUser().getUserId()).blockingFirst().get(0).getProductId());

        view.findViewById(R.id.finish_order).setOnClickListener(v ->
                AndroidNetworking.post("http://192.168.0.5:8080/api/order")
                        .addApplicationJsonBody(orderDto)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        })
        );
    }

}
