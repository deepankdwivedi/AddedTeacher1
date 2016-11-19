package com.added.addedteacher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;

import java.util.ArrayList;

import model.ChapterLectureModel;


	public class SubjectChapter extends Fragment {
		ListView class_subject;
		MyDatabase db;
		ArrayList<ChapterLectureModel> chapters;
		ListView chaptersListView;
		String classid,secid,subid,sub_name;
		LayoutInflater layoutInflater;
		TextView subjectHeading,classHeading;
		public String class_name;
		public String sec_name;
		public TextView secHeading;


		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View v=inflater.inflate(R.layout.subject_chapter,container,false);
			layoutInflater=inflater;
			Bundle bundle=this.getArguments();
			classid=bundle.getString("classid");
			secid=bundle.getString("secid");
			subid=bundle.getString("subid");
			sub_name=bundle.getString("sub_name");
			class_name=bundle.getString("class_name");
			sec_name=bundle.getString("sec_name");
			System.out.println("class_id "+classid+" sec_id "+secid+" sub_id "+subid);
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			db= MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
			subjectHeading= (TextView) getView().findViewById(R.id.subjectheading);
			classHeading= (TextView) getView().findViewById(R.id.classheading);
			secHeading= (TextView) getView().findViewById(R.id.secheading);
			subjectHeading.setText(sub_name);
			classHeading.setText(class_name);
			secHeading.setText(sec_name);
			chapters=db.getChaptersAndLecturesFromSubjectName(classid,secid,subid);

			chaptersListView=(ListView) getView().findViewById(R.id.chaptersListView);
			ChaptersAdapter chaptersAdapter=new ChaptersAdapter(chapters);
			chaptersListView.setAdapter(chaptersAdapter);
			chaptersListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
										long arg3) {
					// TODO Auto-generated method stub
				/*Intent intent=new Intent(SubjectChapter.this, LecturesView.class);
				intent.putExtra("ch_id", ChapterLectureModel.chapter_totalLectures.get(arg2).chap__id);
				intent.putExtra("total_lectures", ChapterLectureModel.chapter_totalLectures.get(arg2).totalLectures);
				startActivity(intent);*/
					LecturesView fragment = new LecturesView();
					Bundle bundle = new Bundle();
					bundle.putInt("ch_id", ChapterLectureModel.chapter_totalLectures.get((arg2)).chap__id);
					System.out.println("chapter_id:"+ChapterLectureModel.chapter_totalLectures.get((arg2)).chap__id);
					bundle.putInt("total_lectures", ChapterLectureModel.chapter_totalLectures.get((arg2)).totalLectures);
					fragment.setArguments(bundle);
					getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();

				}
			});
		}

		public class ChaptersAdapter extends BaseAdapter
		{
			LayoutInflater  inflater;
			//ArrayList<ChapterLectureModel> chaptersArrayList;

			public ChaptersAdapter(ArrayList<ChapterLectureModel> chapters) {
				// TODO Auto-generated constructor stub
				//chaptersArrayList=chapters;
				inflater=(LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return ChapterLectureModel.chapter_totalLectures.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				ViewHolder holder=null;
				if(convertView == null)
				{
					convertView=inflater.inflate(R.layout.chapters, null);
					holder=new ViewHolder();
					holder.chapterTextView=(TextView) convertView.findViewById(R.id.chaptersTextView);
					holder.totalLecturesTextView=(TextView) convertView.findViewById(R.id.totalLectures);
					convertView.setTag(holder);
				}
				else{
					holder=(ViewHolder) convertView.getTag();
				}
				holder.totalLecturesTextView.setText(ChapterLectureModel.chapter_totalLectures.get(position).totalLectures+"");
				holder.chapterTextView.setText(ChapterLectureModel.chapter_totalLectures.get(position).ch_name);
				return convertView;
			}
			public class ViewHolder
			{
				TextView chapterTextView;
				TextView totalLecturesTextView;
			}

		}

	}
