package com.example.javafunctional;

import com.example.javafunctional.entity.Account1;
import com.example.javafunctional.entity.Account2;
import com.example.javafunctional.entity.ResponseApi;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import lombok.*;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class JavaFunctionalApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JavaFunctionalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("rx KAVA");

//		observablePushObject();

//		observablePushEvents();
//		creatingSourceObservable();
//		sleep(2000);
//		implementingAndSubscribingToAnObserver();
		implementingAndSubscribingToAnObserverWithLambdaExpresion();

	}



	public Single<ResponseApi> process(){
		List<Account1> account1s = findAccountByContract();
		List<Account2> account2s = findAccountByUser();
		//return Single.zip( account1s , account2s , this::processRsp);
//        Single.zip(accountUserPowerResponseObservable.toList(),
//                accountResponseObservableMerge.toList(), this::process).toObservable()
//                .flatMap(Single::toObservable)
//                .flatMap(Observable::fromIterable);
//

//		 return Single.just(null);
		return Single.just(new ResponseApi());
	}


	private Single<ResponseApi> processRsp(List<Account1> account1s, List<Account2> account2s ) {
		return null;
	}


	private Single<String> createThread() {

		Observable<Account1>  ob1 = Observable.just(Account1.builder().code("c1").company("e1").build()).subscribeOn(Schedulers.computation());
		Observable<Account1>  ob2 = Observable.just(Account1.builder().code("c2").company("e2").build());
		Observable<Account1>  ob3 = Observable.just(Account1.builder().code("c3").company("e3").build());


		return null;
	}


	public void observablePushObject() {

		Observable<Account1>  ob1 = Observable.just(Account1.builder().code("c1").company("e1").build());
		Observable<Account1>  ob2 = Observable.just(Account1.builder().code("c2").company("e2").build());
		Observable<Account1>  ob3 = Observable.just(Account1.builder().code("c3").company("e3").build());

		Action onEnd = () -> System.out.println("fUCK");


		Observable.merge(ob1,ob2,ob3)
				.map(obj -> obj.getCompany().length())
				.doOnComplete(onEnd)
				.subscribe(s -> System.out.println("n=" + s));




		//S is the argument

	}

	private void creatingSourceObservable() {

		Observable<String> source = Observable.create( emmiter ->
				{
					emmiter.onNext("xxx");
					emmiter.onNext("kkk");
					emmiter.onNext("zzz");
					emmiter.onComplete();
				}
		);

		source.subscribe(s -> System.out.println(s));


	}


	private void implementingAndSubscribingToAnObserver() {
		Observable<String> source =
				Observable.just("Alpha", "Beta", "Gamma", "Delta",
						"Epsilon");
		Observer<Integer> myObserver = new Observer<Integer>() {
			@Override
			public void onSubscribe(Disposable d) {
//do nothing with Disposable, disregard for now
			}
			@Override
			public void onNext(Integer value) {
				System.out.println("RECEIVED: " + value);
			}
			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}
			@Override
			public void onComplete() {
				System.out.println("Done!");
			}
		};
		source.map(String::length).filter(i -> i >= 5)
				.subscribe(myObserver);
	}


	private void implementingAndSubscribingToAnObserverWithLambdaExpresion() {
		Observable<String> source =
				Observable.just("Alpha", "Beta", "Gamma", "Delta",
						"Epsilon");

		Consumer<String> onNext = i -> System.out.println("Received:" +i);
		Action onComplete = () -> System.out.println("Done!");
		Consumer<Throwable> onError = Throwable::printStackTrace;

		source.subscribe(onNext,onError,onComplete);

	}





	private void creatingSourceObservableWithTryCatch() {
		Observable<String> source = Observable.create( emmiter ->
				{
					try{
						emmiter.onNext("xxx");
						emmiter.onNext("kkk");
						emmiter.onNext("zzz");
						emmiter.onComplete();
					}catch (Throwable ex){
						emmiter.onError(ex);
					}

				});
		source.subscribe(s -> System.out.println(s), Throwable::printStackTrace);

	}


	private void observablePushEvents() {
		Observable<Long> secondIntervals = Observable.interval(1, TimeUnit.SECONDS);


		secondIntervals.subscribe( s -> System.out.println(s));


	}


	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private void test(Single<Account1> in) {

	 	Maybe.fromSingle(in)
			.defaultIfEmpty(Account1.builder().company("fuch").build())
				;


	}



	private Single<String> join(Single<List<Account1>> c1, Single<List<Account1>> c2){
		return Single.just(c1.blockingGet().get(0).getCompany() + c2.blockingGet().get(0).getCode());
	}

//	private <R, T1, T2> R processRsp(T1 a, T2 b) {
//
//	}


//	private Single<ResponseApi> processRsp(List<Account1> account1s, List<Account2> account2s) {
//		account1s.get(1);
//		account2s.get(1);
//		return Single.just(ResponseApi.builder().company("1212").build());
//
//	}

	private ResponseApi builAll(GroupedObservable<String,Account3> in){
		return ResponseApi.builder()
				.company(in.getKey())
				.accounts(in.toList().blockingGet())
				.build();
	}


	private Account3 filterRepeteadAccount(List<Account1> ac1, Account2 ac2){
		return
		ac1
		.stream().filter(account1 -> account1.getCode().equalsIgnoreCase(ac2.getCode()))
		.map(account1 -> buildResp(account1,ac2))
		.findFirst()
		.orElse(new Account3());


	}

	private Account3 buildResp(Account1 ac1, Account2 ac2){
		return Account3.builder()
				.company(ac1.getCompany())
				.code(ac2.getCode())
				.account(Account.builder()
				.param1(ac2.getType()).param2(ac1.getType()).build()).build();


	}

	private ResponseApi buildResponse(List<Account3> accounts){
		return ResponseApi.builder()
				.company("company")
				.accounts(accounts)
				.build();
	}

	private List<Account1> findAccountByContract(){
		List<Account1> list;
		Account1 account1 = Account1.builder().code("1").company("A01")
				.type("t1").build();
		Account1 account2 = Account1.builder().code("2").company("A02")
				.type("t2").build();
		Account1 account3 = Account1.builder().code("3").company("A03")
				.type("t3").build();

		Account1 account4 = Account1.builder().code("4").company("A05")
				.type("t1").build();
		Account1 account5 = Account1.builder().code("5").company("A05")
				.type("t2").build();
		Account1 account6 = Account1.builder().code("6").company("A06")
				.type("t3").build();

		Account1 account7 = Account1.builder().code("7").company("A07")
				.type("t1").build();
		Account1 account8 = Account1.builder().code("8").company("A08")
				.type("t2").build();
		Account1 account9 = Account1.builder().code("9").company("A01")
				.type("t3").build();

		list = new ArrayList<>();
		list.add(account1);list.add(account2);list.add(account3);
		list.add(account4);list.add(account5);list.add(account6);
		list.add(account7);list.add(account8);list.add(account9);
		return list;
	}

	private List<Account2> findAccountByUser(){
		List<Account2> list;
		Account2 account1 = Account2.builder().code("2")
				.type("t1").build();
		Account2 account2 = Account2.builder().code("5")
				.type("t2").build();
		Account2 account3 = Account2.builder().code("9")
				.type("t3").build();
		Account2 account4 = Account2.builder().code("4")
				.type("t3").build();
		Account2 account5 = Account2.builder().code("1")
				.type("t3").build();
		list = new ArrayList<>();
		list.add(account1);list.add(account2);list.add(account3);
		list.add(account4);list.add(account5);
		return list;
	}




//	private Single<Response> fillResponse(){
//		List<Account1> list;
//		Account1 account1 = Account1.builder().code("1")
//				.type("t1").build();
//		Account1 account2 = Account1.builder().code("2")
//				.type("t2").build();
//		Account1 account3 = Account1.builder().code("3")
//				.type("t3").build();
//		 list = new ArrayList<>();
//		 list.add(account1);list.add(account2);list.add(account3);
//
//		return Single.just(Response.builder().accountList(list).type("res")
//				.code("01").build());
//	}
//
//	private void response(){
//		 fillResponse()
//				.map(response -> response.getAccountList())
//				.toObservable()
//				.flatMapIterable(x->x)
//				.map(z->z.getCode())
//				.take(2)
//				.create(x -> {
//					x.onNext(fuckAndPay("x"));
//					x.onComplete();
//				});
//	}

	private static String fuckAndPay(String fuck){
		return "x"+fuck;
	}


	Subscriber<String> stringSubscriber = new Subscriber<String>() {
		@Override
		public void onSubscribe(Subscription s) {
			System.out.println();
		}

		@Override
		public void onNext(String s) {
			s = s.concat("HI");
		}

		@Override
		public void onError(Throwable t) {

		}

		@Override
		public void onComplete() {
			System.out.println();

		}
	};


	@Builder
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Account{
		String param1;
		String param2;
	}



	@Builder
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Account3{
		String company;
		Account account;
		String code;
	}


	//	@Builder
//	@Getter
//	@Setter
//	@AllArgsConstructor
//	@NoArgsConstructor
//	public static class Account1 {
//		String code;
//		String type;
//		String company;
//	}
//	@Builder
//	@Getter
//	@Setter
//	@NoArgsConstructor
//	@AllArgsConstructor
//	public static class Account2{
//		String code;
//		String type;
//
//
//	}

//	@Builder
//	@Getter
//	@Setter
//	public static class Response{
//		String code;
//		String type;
//		List<Account1> accountList;
//	}

}
