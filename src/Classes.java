import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


class nodeHuffman{
	int data;
	ArrayList<Integer> Hcode=new ArrayList<Integer>();
	void treeTrav(node root,ArrayList<Integer> l,ArrayList<nodeHuffman> codeTable){
		if(root == null) return;
		else if(root.left == null && root.right == null){
			nodeHuffman n=new nodeHuffman();
			n.data=root.data;
			for(int i=0;i<l.size();i++){
				n.Hcode.add(l.get(i));
			}
			codeTable.add(n);
		}
		else{
			l.add(0);
			treeTrav(root.left, l, codeTable);
			l.set(l.size()-1, 1);
			treeTrav(root.right, l, codeTable);
			l.remove(l.size()-1);
		}
	}
}

class node{
	int data;
	long freq;
	node left;
	node right;
	node(){
		data=0;
		freq=0;
		left=null;
		right=null;
		
	}
	int getData(){
		return this.data;
	}
	long getFreq(){
		return this.freq;
	}
}

class binaryHeap{
	int left(int i){
		return (2*i)+1;
	}
	int right(int i){
		return (2*i)+2;
	}
	int parent(int i){
		return (i/2) - 1;
	}
	void heapify(ArrayList<node> l,int i,int m){
		int le=left(i);
		int re=right(i);
		int min=i;
		
		if(le<m && l.get(le).getFreq()<l.get(min).getFreq()) min=le;
		if(re<m && l.get(re).getFreq()<l.get(min).getFreq()) min=re;
		if(min!=i){
			Collections.swap(l,i,min);
			this.heapify(l,min,m);
		}
	}
	node extract_min(ArrayList<node> l){
		node temp=l.get(0);
		Collections.swap(l,0,l.size()-1);
		l.remove(l.size()-1);
		this.heapify(l,0,l.size());
		return temp;
	}
	void buildHeap(ArrayList<node> l, int n){
		for(int i=(n/2)-1;i>=0;i--){
			this.heapify(l,i,n);
		}	
	}
	node build_tree_using_binary_heap(ArrayList<node> l){
		ArrayList<node> temp=new ArrayList<node>(l);
		this.buildHeap(temp,temp.size());
		node root=new node();
		while(temp.size()>1){
			node n1=this.extract_min(temp);
			node n2=this.extract_min(temp);
			node n3=new node();
			n3.data=-1;
			n3.freq=n1.freq+n2.freq;
			n3.left=n1;
			n3.right=n2;
			temp.add(n3);
			int i=temp.size();
			while(i>1 && temp.get(parent(i)).getFreq() > temp.get(i-1).getFreq() ){
				Collections.swap(temp, i-1, parent(i));
				i=parent(i);
			}
			if(temp.size()==1) root=n3;
			
		}
		this.genCodeTable(root);
		return root;
	}
	void genCodeTable(node root){
		nodeHuffman nH=new nodeHuffman();
		ArrayList<nodeHuffman> codeTable = new ArrayList<nodeHuffman>();
		nH.treeTrav(root, nH.Hcode, codeTable);		
	}
}


class fourWayHeap{
	void fourWayHeapify(ArrayList<node> l,int i,int m){
		int child1=4*(i-2);
		int child2=4*(i-2)+1;
		int child3=4*(i-2)+2;
		int child4=4*(i-2)+3;
		int min=i;
		if(child1 < m && l.get(child1).getFreq()<l.get(min).getFreq()) min=child1;
		if(child2 < m && l.get(child2).getFreq()<l.get(min).getFreq()) min=child2;
		if(child3 < m && l.get(child3).getFreq()<l.get(min).getFreq()) min=child3;
		if(child4 < m && l.get(child4).getFreq()<l.get(min).getFreq()) min=child4;
		if(min!=i){
			Collections.swap(l,i,min);
			this.fourWayHeapify(l,min,m);
			
		}		
	}
	node fourWayExtract_min(ArrayList<node> l){
		node temp=l.get(3);
		Collections.swap(l,3,l.size()-1);
		l.remove(l.size()-1);
		this.fourWayHeapify(l, 3, l.size());
		return temp;		
	}
	void buildFourwayHeap(ArrayList<node> l,int n){
		for(int i=((n-1)/4)+2; i>=3; i--){
			this.fourWayHeapify(l, i, n);
		}
	}
	
	node build_tree_using_4way_heap(ArrayList<node> l) throws IOException{
		ArrayList<node> temp=new ArrayList<node>(l);
		this.buildFourwayHeap(temp, temp.size());
		node root=new node();
		while(temp.size()>4){
			node n1=this.fourWayExtract_min(temp);
			node n2=this.fourWayExtract_min(temp);
			node n3=new node();
			n3.data=-1;
			n3.freq=n1.freq+n2.freq;
			n3.left=n1;
			n3.right=n2;
			temp.add(n3);
			int i=temp.size();
			while(i>4 && temp.get(((i-1)/4)+2).getFreq() > temp.get(i-1).getFreq()){
				Collections.swap(temp, i-1, ((i-1)/4)+2);
				i=((i-1)/4)+2;
			}
			if(temp.size()==4) root=n3;
		}
	this.genCodeTable(root);
	return root;	
	}
	void genCodeTable(node root) throws IOException{
		nodeHuffman nH=new nodeHuffman();
		ArrayList<nodeHuffman> codeTable = new ArrayList<nodeHuffman>();
		nH.treeTrav(root, nH.Hcode, codeTable);		
		this.writeToFile(codeTable);
	}
	void writeToFile(ArrayList<nodeHuffman> codeTable) throws IOException{
		String FILENAME="/home/kps/workspace/ADSProject/src/code_table.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		fw = new FileWriter(FILENAME);
		bw = new BufferedWriter(fw);
		try {
			String content = "";
			for(int j=0;j<codeTable.size();j++){
				//System.out.print(codeTable.get(j).data+"-->");
				content+=codeTable.get(j).data+" " ;
				for(int k=0;k<codeTable.get(j).Hcode.size();k++){
					//System.out.print(codeTable.get(j).Hcode.get(k));
					content+=codeTable.get(j).Hcode.get(k);
				}
				System.out.println(content);
				content="";
				bw.write(content);
				
			}

			
			

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} 
	}
}

public class Classes {

}
