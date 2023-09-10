package matching;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

class Order {
	private int shares;
	private double price;

	public Order(int shares, double price) {
		this.shares = shares;
		this.price = price;
	}

	public int getShares() {
		return shares;
	}

	public double getPrice() {
		return price;
	}

	public void reduceShares(int reduction) {
		shares -= reduction;
	}
}

public class Main {
	private static List<Order> bidOrders = new ArrayList<>();
	private static List<Order> askOrders = new ArrayList<>();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		while (true) {
			System.out.print("Enter 'B' for bid, 'A' for ask, or 'Q' to quit: ");
			String input = scanner.next().toUpperCase();

			if (input.equals("Q")) {
				break;
			}

			if (input.equals("B") || input.equals("A")) {
				System.out.print("Enter number of shares: ");
				int shares = scanner.nextInt();

				System.out.print("Enter price: ");
				double price = scanner.nextDouble();

				if (input.equals("B")) {
					bidOrders.add(new Order(shares, price));
					bidOrders.sort(Comparator.comparingDouble(Order::getPrice).reversed());
					matchOrders("A", price, shares);
				} else {
					askOrders.add(new Order(shares, price));
					askOrders.sort(Comparator.comparingDouble(Order::getPrice));
					matchOrders("B", price, shares);
				}

				displayOrderBook();
			} else {
				System.out.println("Invalid input.");
			}
		}
	}

	private static void matchOrders(String oppositeType, double price, int shares) {
		List<Order> ordersToMatch = oppositeType.equals("B") ? bidOrders : askOrders;
		Iterator<Order> iterator = ordersToMatch.iterator();

		while (iterator.hasNext()) {
			Order order = iterator.next();
			if ((oppositeType.equals("B") && order.getPrice() >= price) ||
					(oppositeType.equals("A") && order.getPrice() <= price)) {
				int matchedShares = Math.min(shares, order.getShares());
				order.reduceShares(matchedShares);
				shares -= matchedShares;

				if (order.getShares() == 0) {
					iterator.remove();
				}

				if (shares == 0) {
					break;
				}
			}
		}
	}

	private static void displayOrderBook() {
		System.out.println("\nOrder Book:");
		System.out.println("BID\tShares\tPrice\tASK\tShares\tPrice");

		int maxOrders = Math.max(bidOrders.size(), askOrders.size());

		for (int i = 0; i < maxOrders; i++) {
			String bidString = i < bidOrders.size() ?
					"\t" + bidOrders.get(i).getShares() + "\t" + bidOrders.get(i).getPrice() :
						"\t\t";

			String askString = i < askOrders.size() ?
					askOrders.get(i).getShares() + "\t" + askOrders.get(i).getPrice() :
						"\t\t";

			System.out.println(bidString + "\t\t" + askString);
		}

		System.out.println();
	}
}