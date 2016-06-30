package com.xskj.shifubang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zshifu.R;
import com.xskj.shifubang.activities.AuthenticationActivity;
import com.xskj.shifubang.activities.ExplainActivity;
import com.xskj.shifubang.activities.FeedbackActivity;
import com.xskj.shifubang.activities.InformationActivity;
import com.xskj.shifubang.activities.LocationDetailsActivity;
import com.xskj.shifubang.activities.LoginActivity;
import com.xskj.shifubang.cropimage.ChooseDialog;
import com.xskj.shifubang.cropimage.CropHelper;
import com.xskj.shifubang.cropimage.OSUtils;
import com.xskj.shifubang.view.CircleImageView;
//�����õ�λ�����ѹ��ܣ���Ҫimport����

public class Personal extends Fragment implements OnClickListener {
	public static CropHelper mCropHelper;
	public static ChooseDialog mDialog;
	public static CircleImageView circleImageView; // ͷ��
	private TextView nicknameTv; // �ǳ�
	private TextView totalOrderTv; // �����ܶ�
	private TextView orderAmountTv; // �������
	private LinearLayout authenticationLlyt; // ��֤
	//private LinearLayout positionLlyt; // λ��
	//private LinearLayout explainLlyt; // ˵��
	//private LinearLayout feedbackLlyt; // ����
	private LinearLayout updateLlyt; // ����
	//private LinearLayout dataLlyt; // ����
	//private LinearLayout atyLlyt; // �
	//private LinearLayout noticeLlyt; // ����
	private LinearLayout cancellationLlyt; // ע��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.first_personal, null);
		init(v);
		return v;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init(View v) {
		nicknameTv = (TextView) v.findViewById(R.id.tv_personal_name);
		totalOrderTv = (TextView) v.findViewById(R.id.tv_personal_total_order);
		orderAmountTv = (TextView) v
				.findViewById(R.id.tv_personal_order_amount);

		mCropHelper = new CropHelper(getActivity(),
				OSUtils.getSdCardDirectory() + "/sfb_head.png");
		mDialog = new ChooseDialog(getActivity(), mCropHelper);
		circleImageView = (CircleImageView) v
				.findViewById(R.id.img_personal_head);

		authenticationLlyt = (LinearLayout) v
				.findViewById(R.id.llyt_personal_authentication);
	/*	positionLlyt = (LinearLayout) v
				.findViewById(R.id.llyt_personal_position);
		explainLlyt = (LinearLayout) v.findViewById(R.id.llyt_personal_explain);
		feedbackLlyt = (LinearLayout) v
				.findViewById(R.id.llyt_personal_feedback);*/
		updateLlyt = (LinearLayout) v.findViewById(R.id.llyt_personal_update);
	/*	dataLlyt = (LinearLayout) v.findViewById(R.id.llyt_personal_data);
		atyLlyt = (LinearLayout) v.findViewById(R.id.llyt_personal_aty);
		noticeLlyt = (LinearLayout) v.findViewById(R.id.llyt_personal_notice);*/
		cancellationLlyt = (LinearLayout) v
				.findViewById(R.id.llyt_personal_cancellation);
	
		circleImageView.setOnClickListener(this);
		authenticationLlyt.setOnClickListener(this);
		/*positionLlyt.setOnClickListener(this);
		explainLlyt.setOnClickListener(this);
		feedbackLlyt.setOnClickListener(this);*/
		updateLlyt.setOnClickListener(this);
	/*	dataLlyt.setOnClickListener(this);
		atyLlyt.setOnClickListener(this);
		noticeLlyt.setOnClickListener(this);*/
		cancellationLlyt.setOnClickListener(this);

		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_personal_head:
			mDialog.popSelectDialog();
			break;
		case R.id.llyt_personal_authentication:
			Toast.makeText(getActivity(), "������֤ͨ����", 0).show();
			/*startActivity(new Intent(getActivity(),
					AuthenticationActivity.class));*/
			break;
	/*	case R.id.llyt_personal_position:
			// ��ת��λ���������
			startActivity(new Intent(getActivity(), LocationDetailsActivity.class));
			
			break;
		case R.id.llyt_personal_explain:
			startActivity(new Intent(getActivity(), ExplainActivity.class));

			break;
		case R.id.llyt_personal_feedback:
			startActivity(new Intent(getActivity(), FeedbackActivity.class));

			break;*/
		case R.id.llyt_personal_update:
			Toast.makeText(getActivity(), "��ǰ�汾�����°汾��", 0).show();
			break;
	/*	case R.id.llyt_personal_data:
			//startActivity(new Intent(getActivity(), InformationActivity.class));

			break;*/

		case R.id.llyt_personal_cancellation:
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;

		default:
			break;
		}
	}
	
}
