package api.component.type

@JvmInline
value class ChannelId(val value: Int) {
    override fun toString(): String = value.toString()
}