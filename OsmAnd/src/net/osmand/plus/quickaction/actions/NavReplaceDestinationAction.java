package net.osmand.plus.quickaction.actions;

import static net.osmand.plus.quickaction.QuickActionIds.NAV_REPLACE_DESTINATION_ACTION_ID;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.osmand.data.LatLon;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.quickaction.QuickAction;
import net.osmand.plus.quickaction.QuickActionType;

public class NavReplaceDestinationAction extends SelectMapLocationAction {

	public static final QuickActionType TYPE = new QuickActionType(NAV_REPLACE_DESTINATION_ACTION_ID,
			"nav.destination.replace", NavReplaceDestinationAction.class)
			.nameRes(R.string.quick_action_destination).iconRes(R.drawable.ic_action_point_add_destination).nonEditable()
			.category(QuickActionType.NAVIGATION).nameActionRes(R.string.shared_string_replace);

	public NavReplaceDestinationAction() {
		super(TYPE);
	}

	public NavReplaceDestinationAction(QuickAction quickAction) {
		super(quickAction);
	}

	@Override
	protected void onLocationSelected(@NonNull MapActivity mapActivity, @NonNull LatLon latLon) {
		mapActivity.getMapActions().replaceDestination(latLon);
	}

	@Override
	@Nullable
	protected Object getLocationIcon(@NonNull MapActivity mapActivity) {
		return mapActivity.getMapLayers().getNavigationLayer().getPointToNavigateIcon();
	}

	@Override
	public void drawUI(@NonNull ViewGroup parent, @NonNull MapActivity mapActivity) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.quick_action_with_text, parent, false);

		((TextView) view.findViewById(R.id.text)).setText(
				R.string.quick_action_replace_destination_desc);
		parent.addView(view);
	}
}
