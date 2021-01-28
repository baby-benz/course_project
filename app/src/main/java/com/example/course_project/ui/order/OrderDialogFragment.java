package com.example.course_project.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Calendar;

public class OrderDialogFragment extends BottomSheetDialogFragment {
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

        view.findViewById(R.id.finish_order).setOnClickListener(v ->
                AndroidNetworking.post("http://localhost:8080/api/order")
                        .addBodyParameter("orderId", "1")
                        .addBodyParameter("Коля, че по заказам?", "ММ?")
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
