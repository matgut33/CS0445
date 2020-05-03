//Matthew Gutkin
//CS0445 Spring 2020
//Assignment 1, SimBank class

import java.util.*;
import java.text.DecimalFormat;

@SuppressWarnings("unchecked")
public class SimBank {
	private int ntells, maxcust, count;
	private double hours, rate, trans;
	private ArrayList<Queue<Customer>> cusQueues = new ArrayList<Queue<Customer>>();
	private ArrayList<Customer> complete = new ArrayList<Customer>();
	private ArrayList<Customer> noStay = new ArrayList<Customer>();
	private PriorityQueue<SimEvent> eventPQ  = new PriorityQueue<SimEvent>();
	private Teller[] tellers;
	private RandDist rand;
	private boolean singlequeue;
	private DecimalFormat df = new DecimalFormat("#.##");

	public SimBank (int ntell, boolean singleq, double hrs, double arr_rate, double t_min, int maxq, long seed) {
		ntells = ntell;
		tellers = new Teller[ntells];
		for (int i = 0; i < ntells; i++) {
			tellers[i] = new Teller(i);
		}
		singlequeue = singleq;
		hours = hrs;
		rate = arr_rate;
		trans = t_min;
		maxcust = maxq;
		rand = new RandDist(seed);
	}

	public void runSimulation() {
		double curr_time = 0;
		SimEvent curr;
		double min = hours * 60;
		double stop_time = curr_time + min;
		double arr_rate_min = rate/60;  // convert arrivals to minutes
		double serve_rate = 1/trans;

		if (singlequeue) {
			Queue<Customer> cusQueue = new LinkedList<Customer>();
			Customer cus;
			double next_arr_min = rand.exponential(arr_rate_min);
			ArrivalEvent next_arrival = new ArrivalEvent(next_arr_min);
			eventPQ.offer(next_arrival);

			while (eventPQ.size() > 0) {
				curr = eventPQ.poll();
				curr_time = curr.get_e_time();

				if (curr instanceof ArrivalEvent) {
					cus = new Customer((count++), curr_time);
					double serve_time = rand.exponential(serve_rate);
					double finish_time = 0;
					cus.setServiceT(serve_time);
					if (cus.getId() < ntells) {
						for (int i = 0; i < ntells; i++) {
							if (!tellers[i].isBusy()) {
								cus.setTeller(i);
								cus.setStartT(curr_time);
								finish_time = serve_time + curr_time;
								cus.setEndT(finish_time);
								tellers[i].addCust(cus);
								CompletionLocEvent comp = new CompletionLocEvent(finish_time, i);
								eventPQ.offer(comp);
								break;
							}
						}
					} else {
						if (cusQueue.size() == maxcust) {
							cus.setServiceT(serve_time);
							noStay.add(cus);
						} else {
							cus.setQueue(0);
							cusQueue.offer(cus);
						}
					}

					next_arr_min = curr_time + rand.exponential(arr_rate_min);
					if (next_arr_min <= stop_time) {
						next_arrival = new ArrivalEvent(next_arr_min);
						eventPQ.offer(next_arrival);
					}
				} else if (curr instanceof CompletionLocEvent) {
					CompletionLocEvent qq = (CompletionLocEvent)curr;
					int nu = qq.getLoc();
					complete.add(tellers[nu].removeCust());
					if (cusQueue.size() > 0) {
						cus = cusQueue.poll();
						cus.setTeller(nu);
						cus.setStartT(curr_time);
						double serve_time = rand.exponential(serve_rate);
						double finish_time = serve_time + curr_time;
						cus.setEndT(finish_time);
						tellers[nu].addCust(cus);
						CompletionLocEvent comp = new CompletionLocEvent(finish_time, nu);
						eventPQ.offer(comp);
					}
				}
			}
		} else {
			for (int i = 0; i < ntells; i++) {
				cusQueues.add(new LinkedList<Customer>());
			}
			Customer cus;
			double next_arr_min = rand.exponential(arr_rate_min);
			ArrivalEvent next_arrival = new ArrivalEvent(next_arr_min);
			eventPQ.offer(next_arrival);
			next_arr_min = next_arrival.get_e_time() + rand.exponential(arr_rate_min);
			next_arrival = new ArrivalEvent(next_arr_min);
			eventPQ.offer(next_arrival);

			while (eventPQ.size() > 0) {
				curr = eventPQ.poll();
				curr_time = curr.get_e_time();

				if (curr instanceof ArrivalEvent) {
					cus = new Customer((count++), curr_time);
					double serve_time = rand.exponential(serve_rate);
					cus.setServiceT(serve_time);
					if (cus.getId() < ntells) {
						for (int i = 0; i < ntells; i++) {
							if (!tellers[i].isBusy()) {
								cus.setTeller(i);
								cus.setStartT(curr_time);
								double finish_time = serve_time + curr_time;
								cus.setEndT(finish_time);
								tellers[i].addCust(cus);
								CompletionLocEvent comp = new CompletionLocEvent(finish_time, i);
								eventPQ.offer(comp);
								break;
							}
						}
					} else {
						int indtmp = -1, smin = (maxcust+1);
						for (int i = 0; i < ntells; i++) {
							if (cusQueues.get(i).size() < smin) {
								indtmp = i;
								smin = cusQueues.get(i).size();
							}
						}
						if (smin == maxcust) {
							cus.setServiceT(serve_time);
							noStay.add(cus);
						} else {
							cus.setQueue(indtmp);
							cusQueues.get(indtmp).offer(cus);
						}
					}

					next_arr_min = curr_time + rand.exponential(arr_rate_min);
					if (next_arr_min <= stop_time) {
						next_arrival = new ArrivalEvent(next_arr_min);
						eventPQ.offer(next_arrival);
					}
				} else if (curr instanceof CompletionLocEvent) {
					CompletionLocEvent qq = (CompletionLocEvent)curr;
					int nu = qq.getLoc();
					complete.add(tellers[nu].removeCust());
					if (cusQueues.get(nu).size() > 0) {
						cus = cusQueues.get(nu).poll();
						cus.setTeller(nu);
						cus.setStartT(curr_time);
						double serve_time = rand.exponential(serve_rate);
						double finish_time = serve_time + curr_time;
						cus.setEndT(finish_time);
						tellers[nu].addCust(cus);
						CompletionLocEvent comp = new CompletionLocEvent(finish_time, nu);
						eventPQ.offer(comp);
					}
				}
			}
		}
	}

	public void showResults() {
		double ave_wait = 0, max_wait = 0, std_dev_wait = 0, ave_service = 0, ave_waiter_wait = 0, ave_in_sys = 0, std_dev_tot = 0, mean = 0;
		int waited = 0;
		double[] waits = new double[complete.size()];

		System.out.println();
		System.out.println("Individual Customer Service Information:");
		System.out.printf("%s%15s%15s%15s%15s%15s%15s%15s%15s%n", "Customer", "Arrival", "Service", "Queue", "Teller", "Time Serv", "Time Cust", "Time Serv", "Time Spent");
		System.out.printf("%-5s%15s%15s%15s%15s%15s%15s%15s%15s%n", "ID", "Time", "Time", "Loc", "Loc", "Begins", "Waits", "Ends", "in Sys");
		System.out.printf("%-5s%15s%15s%15s%15s%15s%15s%15s%15s%n", "----", "----", "----", "----", "----", "----", "----", "----", "----");
		for (int i = 0; i < complete.size(); i++) {
			Customer cus = (Customer)complete.get(i);
			ave_wait += cus.getWaitT();
			waits[i] = cus.getWaitT();
			ave_service += cus.getServiceT();
			ave_in_sys += cus.getInSystem();
			if (cus.getWaitT() > max_wait) max_wait = cus.getWaitT();
			if (cus.getWaitT() > 0) {
				waited++;
				ave_waiter_wait += cus.getWaitT();
			}
			System.out.printf("%-5d%15s%15s%15d%15d%15s%15s%15s%15s%n", cus.getId(), df.format(cus.getArrivalT()), df.format(cus.getServiceT()), cus.getQueue(), cus.getTeller(), df.format(cus.getStartT()), df.format(cus.getWaitT()), df.format(cus.getEndT()), df.format(cus.getInSystem()));
		}

		System.out.println();
		System.out.println();
		System.out.println("Customers who did not stay:");
		System.out.printf("%s%15s%15s%n", "Customer", "Arrival", "Service");
		System.out.printf("%-5s%15s%15s%n", "Id", "Time", "Time");
		System.out.printf("%-5s%15s%15s%n", "----", "----", "----");
		for (int i = 0; i < noStay.size(); i++) {
			Customer cus = (Customer)noStay.get(i);
			System.out.printf("%-5d%15s%15s%n", cus.getId(), df.format(cus.getArrivalT()), df.format(cus.getServiceT()));
		}

		std_dev_tot = ave_wait;
		ave_wait /= complete.size();
		ave_service /= complete.size();
		ave_in_sys /= complete.size();
		ave_waiter_wait /= waited;
		int ncus = complete.size() + noStay.size();

		//CALCULATING STANDARD DEV
		mean = std_dev_tot / complete.size();
		double standardDeviation = 0.0;
        int length = waits.length;
        mean = std_dev_tot/length;
        for(double num: waits) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        std_dev_wait =  Math.sqrt(standardDeviation/length);

		System.out.println("Number of Tellers: " + ntells);
		if (singlequeue) System.out.println("Number of Queues: 1");
		else System.out.println("Number of Queues: " + ntells);
		System.out.println("Max number allowed to wait: " + maxcust);
		System.out.println("Customer arrival rate (per hr): " + rate);
		System.out.println("Customer service time (ave min): " + trans);
		System.out.println("Number of customers arrived: " + ncus);
		System.out.println("Number of customers served: " + complete.size());
		System.out.println("Num. Turned Away: " + noStay.size());
		System.out.println("Num. who waited: " + waited);
		System.out.println("Average Wait: " + ave_wait + " min.");
		System.out.println("Max Wait: " + max_wait + " min.");
		System.out.println("Std. Dev. Wait: " + std_dev_wait);
		System.out.println("Ave. Service: " + ave_service + " min.");
		System.out.println("Ave. Waiter Wait: " + ave_waiter_wait + " min.");
		System.out.println("Ave. in System: " + ave_in_sys + " min.");
	}
}