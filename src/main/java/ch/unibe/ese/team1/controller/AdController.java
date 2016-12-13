package ch.unibe.ese.team1.controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team1.controller.pojos.ForbiddenException;
import ch.unibe.ese.team1.controller.pojos.forms.BetForm;
import ch.unibe.ese.team1.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.controller.service.BetService;
import ch.unibe.ese.team1.controller.service.BookmarkService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.User;

/**
 * This controller handles all requests concerning displaying ads and
 * bookmarking them.
 */
@Controller
public class AdController {

	@Autowired
	private AlertService alertService;
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private BetService betService;

	@Autowired
	private VisitService visitService;

	/** Gets the ad description page for the ad with the given id. */
	@RequestMapping(value = "/ad", method = RequestMethod.GET)
	public ModelAndView ad(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		String loggedInUserEmail = (principal == null) ? "" : principal.getName();
		model.addObject("loggedInUserEmail", loggedInUserEmail);

		model.addObject("visits", visitService.getVisitsByAd(ad));
		
		return model;
	}

	/**
	 * Gets the ad description page for the ad with the given id and also
	 * validates and persists the message passed as post data.
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	public ModelAndView messageSent(@RequestParam("id") long id,
			@Valid MessageForm messageForm, BindingResult bindingResult) {

		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		if (!bindingResult.hasErrors()) {
			messageService.saveFrom(messageForm);
		}
		return model;
	}

	/**
	 * Checks if the adID passed as post parameter is already inside user's
	 * List bookmarkedAds. In case it is present, true is returned changing
	 * the "Bookmark Ad" button to "Bookmarked". If it is not present it is
	 * added to the List bookmarkedAds.
	 * 
	 * @return 0 and 1 for errors; 3 to update the button to bookmarked 3 and 2
	 *         for bookmarking or undo bookmarking respectively 4 for removing
	 *         button completly (because its the users ad)
	 */
	@RequestMapping(value = "/bookmark", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int isBookmarked(@RequestParam("id") long id,
			@RequestParam("screening") boolean screening,
			@RequestParam("bookmarked") boolean bookmarked, Principal principal) {
		// should never happen since no bookmark button when not logged in
		if (principal == null) {
			return 0;
		}
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			// that should not happen...
			return 1;
		}
		Set<Ad> bookmarkedAdsIterable = user.getBookmarkedAds();
		if (screening) {
			for (Ad ownAdIterable : adService.getAdsByUser(user)) {
				if (ownAdIterable.getId() == id) {
					return 4;
				}
			}
			for (Ad adIterable : bookmarkedAdsIterable) {
				if (adIterable.getId() == id) {
					return 3;
				}
			}
			return 2;
		}

		Ad ad = adService.getAdById(id);

		return bookmarkService.getBookmarkStatus(ad, bookmarked, user);
	}

	/**
	 * Fetches information about bookmarked houses and own ads and attaches this
	 * information to the myHouses page in order to be displayed.
	 */
	@RequestMapping(value = "/profile/myHouses", method = RequestMethod.GET)
	public ModelAndView myHouses(Principal principal) {
		ModelAndView model;
		User user;
		model = new ModelAndView("myHouses");
		String username = principal.getName();
		user = userService.findUserByUsername(username);

		Iterable<Ad> ownAds = adService.getAdsByUser(user);

		model.addObject("ownAdvertisements", ownAds	);
		return model;
	}
	
	/**
	 * Fetches information about bookmarked houses and attaches this
	 * information to the myBookmarks page in order to be displayed.
	 */
	@RequestMapping(value ="/profile/myBookmarks", method = RequestMethod.GET)
	public ModelAndView myBookmarks(Principal principal){
		ModelAndView model;
		User user;
		model = new ModelAndView("myBookmarks");
		String username = principal.getName();
		user = userService.findUserByUsername(username);

		model.addObject("bookmarkedAdvertisements", user.getBookmarkedAds());
		return model;
	}

	/**
	 * make bet for an ad
	 * 
	 * @param messageForm
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/makeBet", method = RequestMethod.POST)
	public ModelAndView makeBet(
			@RequestParam("id") long id,
			@Valid @ModelAttribute("betForm") BetForm betForm, 
			BindingResult bindingResult, 
			RedirectAttributes redirectAttributes,
			Principal principal) {

		
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		
		Ad ad = adService.getAdById(id);
			
		switch (betService.validateBet(betForm, ad, user)) {
			case BetService.VALIDATE_PRICE_TO_LOW:
				bindingResult.rejectValue("price", "Price is too low", "Price is too low");
			break;
			case BetService.VALIDATE_SAME_USER:
				bindingResult.rejectValue("price", "You can't bet twice", "You can't bet twice");
			break;
			case BetService.VALIDATE_AUCTION_ENDED:
				bindingResult.rejectValue("price", "Auction has ended", "Auction has ended");
			break;
				
		}
		
		ModelAndView model = new ModelAndView("adDescription");
		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());
		
		if (!bindingResult.hasErrors()) {
			Bet bet = this.betService.saveFrom(betForm, ad, user);

			alertService.triggerAlerts(bet);
			model = new ModelAndView("redirect:/ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute(
					"confirmationMessage",
					"Your bet has been placed!"
			);
		}
		
		return model;
	}
	
	/**
	 * place an ad to the homepage 
	 * 
	 * @param id
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/ad/placeOnHomepage", method = RequestMethod.GET)
	public ModelAndView placeOnHomepageForm(
			@RequestParam("id") long id,
			Principal principal) {

		Ad ad = adService.getAdById(id);

		String username = principal.getName();
		User user = userService.findUserByUsername(username);

		if (!user.equals(ad.getUser())) {
			throw new ForbiddenException();
		}
		ModelAndView model = new ModelAndView("placeOnHomepage");
		return model;
	}
	
	@RequestMapping(value = "/rateAd", method = RequestMethod.GET)
	public ModelAndView rateAuctionAdGet(
			@RequestParam("id") long id,
			@RequestParam("rate") long rateId,
			RedirectAttributes redirectAttributes,
			Principal principal) {

		Ad ad = adService.getAdById(id);

		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		User ratingPerson = userService.findUserById(rateId);
		
		if (ad == null || !ad.isAuction() || !ad.isAuctionEnded()) {
			throw new ForbiddenException();	
		}
		
		if (!user.equals(ad.getUser()) && !user.equals(ad.getLastBiddingUser())) {
			throw new ForbiddenException();	
		}
		
		ModelAndView model = new ModelAndView("rate");
		model.addObject("ad", ad);
		model.addObject("rateUser", ratingPerson);
		return model;
	}
	
	@RequestMapping(value = "/rateAd", method = RequestMethod.POST)
	public ModelAndView rateAuctionAdPost(
			@RequestParam("id") long id,
			@RequestParam("rate") long rateId,
			@RequestParam("rating") String rating,
			RedirectAttributes redirectAttributes,
			Principal principal) {

		Ad ad = adService.getAdById(id);

		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		User ratingPerson = userService.findUserById(rateId);

		if (!ad.isAuction() || !ad.isAuctionEnded()) {
			throw new ForbiddenException();	
		}
		
		if (!user.equals(ad.getUser()) && !user.equals(ad.getLastBiddingUser())) {
			throw new ForbiddenException();	
		}

		ModelAndView model = new ModelAndView("rate");
		model = new ModelAndView("redirect:/ad?id=" + ad.getId());
		if (user.equals(ad.getUser()) && ad.isUserRated()) {
			redirectAttributes.addFlashAttribute(
					"confirmationMessage",
					"You cannot rate the user more than one time"
			);
		} else if (user.equals(ad.getLastBiddingUser()) && ad.isAdvertiserRated()) {
			redirectAttributes.addFlashAttribute(
					"confirmationMessage",
					"You cannot rate the advertiser more than one time"
			);
		} else {
			if (rating.equals("1")) {
				this.userService.like(ratingPerson);
			} else {
				this.userService.dislike(ratingPerson);
			}
			
			if (user.equals(ad.getUser())) {
				this.adService.setUserRated(ad);
			} else {
				this.adService.setAdvertiserRated(ad);
			}

			redirectAttributes.addFlashAttribute(
					"confirmationMessage",
					"Your rating has been saved"
			);
		}
		
		return model;
	}

	@RequestMapping(value = "/ad/placeOnHomepage", method = RequestMethod.POST)
	public ModelAndView placeOnHomepageFormPost(
			@RequestParam("id") long id,
			RedirectAttributes redirectAttributes,
			Principal principal) {

		Ad ad = adService.getAdById(id);

		String username = principal.getName();
		User user = userService.findUserByUsername(username);

		if (!user.equals(ad.getUser())) {
			throw new ForbiddenException();
		}
		
		ModelAndView model = new ModelAndView("placeOnHomepage");

		adService.placeOnHomepage(ad, true);
		model = new ModelAndView("redirect:/ad?id=" + ad.getId());
		redirectAttributes.addFlashAttribute(
				"confirmationMessage",
				"Your ad has been placed to the homepage!"
		);
		return model;
	}
	
	private BetForm betForm;
	
	@ModelAttribute("betForm")
	public BetForm betForm() {
		if (betForm == null) {
			betForm = new BetForm();
		}
		return betForm;
	}
	
}