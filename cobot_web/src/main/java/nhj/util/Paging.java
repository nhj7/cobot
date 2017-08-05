package nhj.util;

public class Paging {
	public int w_size = 5;
	public int p_size = 5;
	public int writing_Count = 0;
	public int cur_Page = 0;
	
	public int pageCount;
	public int pageStart;
	public int pageEnd;
	public boolean isPre;
	public boolean isNext;
	public int writeStart;
	public int writeEnd;
	

	public Paging(int w_size, int p_size, int writing_Count, int cur_Page) {
		super();
		this.w_size = w_size;
		this.p_size = p_size;
		this.writing_Count = writing_Count;
		this.cur_Page = cur_Page;
		
		pageCount = getPage_Count();
		pageStart = getPage_Start();
		pageEnd = getPage_End();
		isPre = isPre();
		isNext = isNext();
		writeStart = getWriting_Start();
		writeEnd = getWriting_End();
		
	}

	public int getPage_Count() {
		return ((writing_Count - 1) / w_size) + 1;
	}

	public int getPage_Start() {
		return ((cur_Page - 1) / p_size) * p_size + 1;
	}

	public int getPage_End() {
		return Math.min(getPage_Start() + p_size - 1, getPage_Count());
	}

	public boolean isPre() {
		return getPage_Start() != 1;
	}

	public boolean isNext() {
		return getPage_End() < getPage_Count();
	}

	public int getWriting_Start() {
		return getWriting_End() - w_size + 1;
	}

	public int getWriting_End() {
		return cur_Page * w_size;
	}

	public static void main(String[] args) {

		// 여러가지 매개변수로 테스트 해보시기 바랍니다.
		Paging pg = new Paging(5, 5, 26, 6);
		// 총 글의 갯수는 select count(*) from board 하면 나오겠죠 ,
		// 현재 보고 있는 페이지 번호는 Default 1, 그리고 밑에 페이징에서 링크 걸린 i가 현재 페이지가 됩니다.

		// Paging pg = new Paging(한 화면에 보여질 글 수 , 페이지 분할 수 , 총 글의 갯수 , 현재 보고 있는
		// 페이지 번호 );

		System.out.println("총 페이지 수 : " + pg.getPage_Count());
		System.out.println("페이지 시작 수  : " + pg.getPage_Start());
		System.out.println("페이지 마지막 수  : " + pg.getPage_End());
		System.out.println("Pre 표시 여부  : " + pg.isPre());
		System.out.println("Next 표시 여부   : " + pg.isNext());
		System.out.println("글 범위 시작 번호   : " + pg.getWriting_Start());
		System.out.println("글 범위 끝 번호   : " + pg.getWriting_End());

		System.out.println(
				"select * from board where no between " + pg.getWriting_Start() + " and " + pg.getWriting_End());
		// 이 셀렉트 결과를 화면에 뿌린 후에

		// 밑에서 페이징을 하면 되겠죠? 이거에 링크를 걸고 i가 현재 페이지 번호로서 링크가 걸리게 되겠죠?
		if (pg.isPre())
			System.out.print(" Pre ");
		for (int i = pg.getPage_Start(); i <= pg.getPage_End(); i++) {
			System.out.print(" " + i + " ");
		}
		if (pg.isNext())
			System.out.print(" Next ");

		// 이런 페이징 클래스를 작성하여 사용하는 것이 여러모로 편리합니다. ~ ㅋㅋ

	}
}
